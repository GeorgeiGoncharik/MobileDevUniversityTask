package xyz.goshanchik.prodavayka.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.threeten.bp.OffsetDateTime
import timber.log.Timber
import xyz.goshanchik.prodavayka.data.database.*
import xyz.goshanchik.prodavayka.data.domain.CartItem
import xyz.goshanchik.prodavayka.data.domain.Category
import xyz.goshanchik.prodavayka.data.domain.Product
import xyz.goshanchik.prodavayka.data.network.ProdavaykaNetwork
import xyz.goshanchik.prodavayka.data.network.asDatabaseModel
import xyz.goshanchik.prodavayka.util.NetManager

class Repository(private val database: CommerceRoomDatabase, private val netManager: NetManager) {

    suspend fun refreshCategories(){
        withContext(Dispatchers.IO){
            Timber.d("refreshCategories is called.")
            netManager.isConnectedToInternet?.let { it ->
                if(it){
                    val request = ProdavaykaNetwork.dao.getCategories()
                    request.categories.forEach { networkCategory ->
                        val check: DatabaseCategory? = database.categoryDao().getCategory(networkCategory.id.toInt())
                        if(check == null){
                            database.categoryDao().insertCategories(networkCategory.asDatabaseModel())
                        }
                        else{
                            val databaseCategory = networkCategory.asDatabaseModel()
                            database.categoryDao().updateCategory(databaseCategory.id ,databaseCategory.name, databaseCategory.description, databaseCategory.pictureUrl)
                        }
                    }
                    val dbIdsDeferred = async { database.categoryDao().getAllCategories().map { databaseCategory ->  databaseCategory.id} }
                    val netIdsDeferred = async { request.categories.map { networkCategory -> networkCategory.id.toInt() } }

                    val netIds = netIdsDeferred.await()
                    val dbIds = dbIdsDeferred.await()

                    dbIds.forEach { id ->
                        if(!netIds.contains(id)){
                            database.categoryDao().deleteCategories(
                                database.categoryDao().getCategory(categoryId = id)
                            )
                        }
                    }
                }
            }
        }
    }

    suspend fun refreshProducts(categoryId: Int){
        withContext(Dispatchers.IO){
            Timber.d("refreshProducts is called.")
            netManager.isConnectedToInternet?.let {
                if(it){
                    val request = ProdavaykaNetwork.dao.getProducts(categoryId)
                    request.products.forEach { networkProduct ->
                        val check: DatabaseProduct? = database.productDao().getProduct(networkProduct.id.toLong())
                        if(check == null){
                            database.productDao().insertProducts(networkProduct.asDatabaseModel())
                        }
                        else
                            database.productDao().updateProducts(networkProduct.asDatabaseModel(recent = check.recent))
                    }

                    val dbIdsDeferred = async { database.productDao().getProducts(categoryId).map { databaseProduct -> databaseProduct.id } }
                    val netIdsDeferred = async { request.products.map { networkProduct -> networkProduct.id.toLong() } }

                    val netIds = netIdsDeferred.await()
                    val dbIds = dbIdsDeferred.await()

                    dbIds.forEach {id ->
                        if(!netIds.contains(id)){
                            database.productDao().deleteProducts(
                                database.productDao().getProduct(id)
                            )
                        }
                    }
                }
            }
        }
    }

    val allCategories: LiveData<List<Category>> = Transformations.map(database.categoryDao().getAllCategoriesLiveData()){
        it.asDomainModel()
    }

    @ExperimentalCoroutinesApi
    val categoriesUsingFlow: Flow<List<Category>> = database.categoryDao().getAllCategoryFlow()
        .onStart { refreshCategories() }
        .map { it.asDomainModel() }
        .flowOn(Dispatchers.IO)

    val allRecents: LiveData<List<Product>> = Transformations.map(database.productDao().getRecents()){
        it.asDomainModel()
    }

    val allCartItems: LiveData<List<CartItem>> = Transformations.map(database.productDao().getDomainCartItems()){
        it.map { db ->
            Timber.d("id(cart) ${db.cartItem.id} product_id: ${db.cartItem.productId}  quantity ${db.cartItem.quantity} id(product): ${db.product.id}")
            CartItem(
                id = db.cartItem.id,
                product = db.product.asDomainModel(),
                category = db.category.asDomainModel(),
                quantity = db.cartItem.quantity
            )
        }
    }

    fun getAllProducts(): List<Product> = database.productDao().getAll().map { it.asDomainModel() }

    fun getCategoryWithProducts(id: Int) = Transformations.map(database.categoryDao().getCategoryWithProducts(id)){
        it.asDomainModel()
    }

    suspend fun addRecent(product: Product){
        withContext(Dispatchers.IO){
            database.productDao().updateProducts(
                DatabaseProduct(product.id, product.name, product.price, product.discount, product.description, product.categoryId, product.pictureUrl, recent = OffsetDateTime.now())
                )
        }
        Timber.d("added to recents ${product.name}")
    }

    suspend fun addItemToCart(product: Product){
        withContext(Dispatchers.IO){
            val existed: DatabaseCartItem? = database.cartDao().getCartItemByProductId(productId = product.id)
            if(existed == null){
                database.cartDao().addCartItem(
                    DatabaseCartItem(
                        productId = product.id,
                        quantity = 1
                    )
                )
            }
            else
                database.cartDao().updateCartItem(existed.apply { quantity += 1 })

        }
    }

    suspend fun deleteCartItem(id: Long){
        withContext(Dispatchers.IO){
            database.cartDao().deleteCartItem(id)
        }
    }

    suspend fun updateCartItem(id: Long, change: Int){
        withContext(Dispatchers.IO){
            val item = database.cartDao().getCartItemById(id)
            if(item.quantity + change < 1)
                database.cartDao().deleteCartItem(id)
            else
                database.cartDao().updateCartItem(
                    databaseCartItem = item.apply {quantity += change}
                )
        }
    }

}