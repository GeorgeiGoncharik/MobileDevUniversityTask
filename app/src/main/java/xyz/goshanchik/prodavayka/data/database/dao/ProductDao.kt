package xyz.goshanchik.prodavayka.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import xyz.goshanchik.prodavayka.data.database.DatabaseProduct
import xyz.goshanchik.prodavayka.data.database.DomainCartItem

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(vararg Products: DatabaseProduct): List<Long>

    @Update
    suspend fun updateProducts(vararg Products: DatabaseProduct)

    @Delete
    suspend fun deleteProducts(vararg Products: DatabaseProduct)

    @Query("SELECT * FROM products WHERE category_id=:categoryId")
    fun getProductsLiveData(categoryId: Int): LiveData<List<DatabaseProduct>>

    @Query("SELECT * FROM products WHERE category_id=:categoryId")
    fun getProductsFlow(categoryId: Int): Flow<List<DatabaseProduct>>

    @Query("SELECT * FROM products WHERE category_id=:categoryId")
    suspend fun getProducts(categoryId: Int): List<DatabaseProduct>

    @Query("SELECT * FROM products WHERE id=:ProductId")
    suspend fun getProduct(ProductId: Long): DatabaseProduct

    @Query("DELETE FROM products")
    fun deleteAll()

    @Query("SELECT * FROM products")
    fun getAll(): List<DatabaseProduct>

    @Query("SELECT * FROM products ORDER BY datetime(recent) DESC LIMIT 5")
    fun getRecents(): LiveData<List<DatabaseProduct>>

    @Transaction
    @Query("SELECT * FROM products JOIN cart on products.id=cart.product_id")
    fun getDomainCartItems(): LiveData<List<DomainCartItem>>
}