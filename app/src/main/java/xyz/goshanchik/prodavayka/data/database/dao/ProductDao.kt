package xyz.goshanchik.prodavayka.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.goshanchik.prodavayka.data.database.DatabaseProduct

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(vararg Products: DatabaseProduct): List<Long>

    @Update
    fun updateProducts(vararg Products: DatabaseProduct)

    @Delete
    fun deleteProducts(vararg Products: DatabaseProduct)

    @Query("SELECT * FROM Products")
    fun getAllProducts(): LiveData<List<DatabaseProduct>>

    @Query("SELECT * FROM Products WHERE category_id=:categoryId")
    fun getProducts(categoryId: Int): LiveData<List<DatabaseProduct>>

    @Query("SELECT * FROM Products WHERE id=:ProductId")
    fun getProduct(ProductId: Long): LiveData<DatabaseProduct>

    @Query("DELETE FROM Products")
    fun deleteAll()

    @Query("DELETE FROM Products WHERE category_id=:categoryId")
    fun deleteOfCategory(categoryId: Int)

    @Query("SELECT * FROM Products WHERE recent=1")
    fun getRecents(): LiveData<List<DatabaseProduct>>
}