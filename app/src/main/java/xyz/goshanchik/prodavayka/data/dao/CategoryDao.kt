package xyz.goshanchik.prodavayka.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.goshanchik.prodavayka.model.Category
import xyz.goshanchik.prodavayka.model.CategoryWithProducts

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(vararg categories: Category): List<Long>

    @Update
    fun updateCategories(vararg categories: Category)

    @Delete
    fun deleteCategories(vararg categories: Category)

    @Query("SELECT * FROM Categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM Categories WHERE id=:categoryId")
    fun getCategory(categoryId: Int): LiveData<Category>

    @Transaction
    @Query("SELECT * FROM Categories")
    fun getCategoriesWithProducts(): LiveData<List<CategoryWithProducts>>

    @Transaction
    @Query("SELECT * FROM Categories WHERE id=:categoryId")
    fun getCategoryWithProducts(categoryId: Int): LiveData<CategoryWithProducts>

    @Query("DELETE FROM Categories")
    fun deleteAll()
}