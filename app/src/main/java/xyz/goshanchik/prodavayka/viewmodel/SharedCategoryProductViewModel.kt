package xyz.goshanchik.prodavayka.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import xyz.goshanchik.prodavayka.WORK_OFFER_TAG
import xyz.goshanchik.prodavayka.data.Repository
import xyz.goshanchik.prodavayka.data.database.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.data.domain.Category
import xyz.goshanchik.prodavayka.data.domain.Product
import xyz.goshanchik.prodavayka.util.NetManager
import xyz.goshanchik.prodavayka.workers.OfferWorker
import java.time.Duration
import java.time.LocalDateTime


class SharedCategoryProductViewModel(private val categoryId: Int, private val productId: Long, application: Application) : AndroidViewModel(
    application
) {
    private val repository = Repository(CommerceRoomDatabase.getDatabase(application), NetManager(application))

    val outputWorkInfos: LiveData<List<WorkInfo>>

    init {
        refreshDataFromRepository()
    }

    private val workManager = WorkManager.getInstance(application)

    val categoryWithProducts = repository.getCategoryWithProducts(categoryId)

    private val _currentProduct: MutableLiveData<Product?> = MutableLiveData(null)
    val currentProduct: LiveData<Product?>
        get() = _currentProduct

    val currentCategory: LiveData<Category> = Transformations.map(categoryWithProducts) {it.category}

    private val _navigateDetailPage = MutableLiveData(false)
    val navigateDetailPage: LiveData<Boolean>
        get() = _navigateDetailPage

    init {
        if(productId == -1L){
            val request = getOfferWorkRequest()
            workManager.enqueue(request)
            Timber.d("work has been enqueued1.")
        }
        else{
            onNavigateToProductItemDetail(productId)
        }
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(WORK_OFFER_TAG)
    }

//    private fun getOfferWorkRequest(): WorkRequest = PeriodicWorkRequestBuilder<OfferWorker>(Duration.ofMinutes(10))
//            .addTag(WORK_OFFER_TAG)
//            .setConstraints(Constraints.Builder()
//                .setRequiresBatteryNotLow(true)
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build())
//            .build()

    private fun getOfferWorkRequest(): WorkRequest = OneTimeWorkRequestBuilder<OfferWorker>()
        .addTag(WORK_OFFER_TAG)
        .setConstraints(Constraints.Builder()
//            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build())
        .build()


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
                _showSnackBar.postValue("${product.id} ${product.name} added to the Cart!")
            }
        }
    }

    fun onNavigateToProductItemDetailDone(){
        _navigateDetailPage.postValue(false)
    }

    fun refreshDataFromRepository() {
        viewModelScope.launch {
            repository.refreshProducts(categoryId)
        }
    }

    fun resetShowSnackBar() {
        _showSnackBar.postValue("")
    }
}