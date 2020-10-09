package xyz.goshanchik.prodavayka.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.goshanchik.prodavayka.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(vararg Products: Product): List<Long>

    @Update
    fun updateProducts(vararg Products: Product)

    @Delete
    fun deleteProducts(vararg Products: Product)

    @Query("SELECT * FROM Products")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM Products WHERE category_id=:categoryId")
    fun getProducts(categoryId: Int): LiveData<List<Product>>

    @Query("SELECT * FROM Products WHERE id=:ProductId")
    fun getProduct(ProductId: Int): LiveData<Product>

    @Query("DELETE FROM Products")
    fun deleteAll()
}