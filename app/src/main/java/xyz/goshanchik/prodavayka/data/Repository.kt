package xyz.goshanchik.prodavayka.data

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.ColumnInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import xyz.goshanchik.prodavayka.data.database.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.data.database.DatabaseProduct
import xyz.goshanchik.prodavayka.data.database.asDomainModel
import xyz.goshanchik.prodavayka.data.domain.CartItem
import xyz.goshanchik.prodavayka.data.domain.Category
import xyz.goshanchik.prodavayka.data.domain.Product
import xyz.goshanchik.prodavayka.data.network.ProdavaykaNetwork
import xyz.goshanchik.prodavayka.data.network.asDatabaseModel
import xyz.goshanchik.prodavayka.util.NetManager
import xyz.goshanchik.prodavayka.util.isOnline
import java.lang.Exception

class Repository(private val database: CommerceRoomDatabase, private val netManager: NetManager) {

    suspend fun refreshCategories(){
        withContext(Dispatchers.IO){
            Timber.d("refreshCategories is called.")
            netManager.isConnectedToInternet?.let {
                if(it){
                    val categories = ProdavaykaNetwork.dao.getCategories()
                    database.categoryDao().deleteAll()
                    database.categoryDao().insertCategories(*(categories.asDatabaseModel()).toTypedArray())
                }
            }
        }
    }

    suspend fun refreshProducts(categoryId: Int){
        withContext(Dispatchers.IO){
            Timber.d("refreshProducts is called.")
            netManager.isConnectedToInternet?.let {
                if(it){
                    val products = ProdavaykaNetwork.dao.getProducts(categoryId)
                    database.productDao().deleteOfCategory(categoryId)
                    database.productDao().insertProducts(*(products.asDatabaseModel()).toTypedArray())
                }
            }
        }
    }

    val allCategories: LiveData<List<Category>> = Transformations.map(database.categoryDao().getAllCategories()){
        it.asDomainModel()
    }

    val allRecents: LiveData<List<Product>> = Transformations.map(database.productDao().getRecents()){
        it.asDomainModel().reversed()
    }

    fun getCategoryWithProducts(id: Int) = Transformations.map(database.categoryDao().getCategoryWithProducts(id)){
        it.asDomainModel()
    }

    suspend fun addRecent(product: Product){
        withContext(Dispatchers.IO){
            database.productDao().updateProducts(
                DatabaseProduct(
                    product.id, product.name, product.price, product.discount, product.description, product.categoryId, product.pictureUrl, recent = 1)
                )
        }
        Timber.d("added to recents ${product.name}")
    }

    suspend fun addItemToCart(product: Product){
        withContext(Dispatchers.IO){

        }
    }

}