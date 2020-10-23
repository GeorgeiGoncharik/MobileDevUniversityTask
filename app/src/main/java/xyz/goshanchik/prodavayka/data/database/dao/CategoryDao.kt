package xyz.goshanchik.prodavayka.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.goshanchik.prodavayka.data.database.DatabaseCategory
import xyz.goshanchik.prodavayka.data.database.DatabaseCategoryWithProducts

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(vararg categories: DatabaseCategory): List<Long>

    @Update
    fun updateCategories(vararg categories: DatabaseCategory)

    @Delete
    fun deleteCategories(vararg categories: DatabaseCategory)

    @Query("SELECT * FROM Categories")
    fun getAllCategories(): LiveData<List<DatabaseCategory>>

    @Query("SELECT * FROM Categories WHERE id=:categoryId")
    fun getCategory(categoryId: Int): LiveData<DatabaseCategory>

    @Transaction
    @Query("SELECT * FROM Categories")
    fun getCategoriesWithProducts(): LiveData<List<DatabaseCategoryWithProducts>>

    @Transaction
    @Query("SELECT * FROM Categories WHERE id=:categoryId")
    fun getCategoryWithProducts(categoryId: Int): LiveData<DatabaseCategoryWithProducts>

    @Query("DELETE FROM Categories")
    fun deleteAll()
}