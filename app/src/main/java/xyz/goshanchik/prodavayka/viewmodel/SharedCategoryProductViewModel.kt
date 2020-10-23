package xyz.goshanchik.prodavayka.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import xyz.goshanchik.prodavayka.data.Repository
import xyz.goshanchik.prodavayka.data.database.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.data.domain.Category
import xyz.goshanchik.prodavayka.data.domain.Product
import xyz.goshanchik.prodavayka.util.NetManager
import java.time.LocalDateTime


class SharedCategoryProductViewModel(private val categoryId: Int, application: Application) : AndroidViewModel(
    application
) {
    private val TAG = "offer_work"

    private val repository = Repository(CommerceRoomDatabase.getDatabase(application, viewModelScope), NetManager(application))

    private val _refreshed: MutableLiveData<LocalDateTime> = MutableLiveData(LocalDateTime.now())
    val refreshed: LiveData<LocalDateTime>
        get() = _refreshed

    init {
        refreshDataFromRepository()
    }

    val categoryWithProducts = repository.getCategoryWithProducts(categoryId)

    private val _currentProduct: MutableLiveData<Product?> = MutableLiveData(null)
    val currentProduct: LiveData<Product?>
        get() = _currentProduct

    val currentCategory: LiveData<Category> = Transformations.map(categoryWithProducts) {it.category}

    private val _navigateDetailPage = MutableLiveData<Boolean>(false)
    val navigateDetailPage: LiveData<Boolean>
        get() = _navigateDetailPage

    fun onNavigateToProductItemDetail(id: Long){
        val product = categoryWithProducts.value?.products?.firstOrNull { it.id == id } ?: throw Exception(
            "There are products in category. What did you press?"
        )
        Timber.d("arg: $id navigateDetailProduct is called. product name: ${product.name}")
        _navigateDetailPage.postValue(true)
        addToRecentProducts(product)
        _currentProduct.postValue(product)
    }

    fun onShowNextProductItem(){
        _currentProduct.value?.let { prev ->
            val product = try {
                categoryWithProducts.value!!.products[categoryWithProducts.value!!.products.indexOf(
                    prev
                ) + 1]
            }
            catch (e: IndexOutOfBoundsException){
                categoryWithProducts.value!!.products.first()
            }
            product.let {
                addToRecentProducts(it)
                _currentProduct.postValue(it)
            }
        }
    }

    fun onShowPrevProductItem(){
        _currentProduct.value?.let { prev ->
            val product = try {
                categoryWithProducts.value!!.products[categoryWithProducts.value!!.products.indexOf(
                    prev
                ) -1]
            }
            catch (e: IndexOutOfBoundsException){
                categoryWithProducts.value!!.products.last()
            }
            product.let {
                addToRecentProducts(it)
                _currentProduct.postValue(it)
            }
        }
    }

    private fun addToRecentProducts(product: Product){
        viewModelScope.launch {
            repository.addRecent(product)
        }
    }

    private val _showSnackBar: MutableLiveData<String> = MutableLiveData("")
    val showSnackBar: LiveData<String>
        get() = _showSnackBar

    fun onProductItemAddToCart(product: Product){
        viewModelScope.launch {
            repository.addItemToCart(product)
            withContext(Dispatchers.Main){
                _showSnackBar.postValue("${product.name} added to the Cart!")
            }
        }
    }

    fun onNavigateToProductItemDetailDone(){
        _navigateDetailPage.postValue(false)
    }

    fun refreshDataFromRepository() {
        viewModelScope.launch {
            repository.refreshProducts(categoryId)
            _refreshed.postValue(LocalDateTime.now())
        }
    }

    fun resetShowSnackBar() {
        _showSnackBar.postValue("")
    }
}