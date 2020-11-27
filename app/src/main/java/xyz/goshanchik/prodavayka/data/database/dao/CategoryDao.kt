package xyz.goshanchik.prodavayka.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import xyz.goshanchik.prodavayka.data.database.DatabaseCategory
import xyz.goshanchik.prodavayka.data.database.DatabaseCategoryWithProducts

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(vararg categories: DatabaseCategory): List<Long>

    @Query("UPDATE categories SET name = :name, description = :description, picture_url =:pictureUrl WHERE id = :id")
    suspend fun updateCategory(id: Int, name: String, description: String, pictureUrl: String)

    @Delete
    suspend fun deleteCategories(vararg categories: DatabaseCategory)

    @Query("SELECT * FROM categories")
    fun getAllCategoriesLiveData(): LiveData<List<DatabaseCategory>>

    @Query("SELECT * FROM categories")
    fun getAllCategoryFlow(): Flow<List<DatabaseCategory>>

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<DatabaseCategory>

    @Query("SELECT * FROM categories WHERE id=:categoryId")
    suspend fun getCategory(categoryId: Int): DatabaseCategory

    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithProducts(): LiveData<List<DatabaseCategoryWithProducts>>

    @Transaction
    @Query("SELECT * FROM categories WHERE id=:categoryId")
    fun getCategoryWithProducts(categoryId: Int): LiveData<DatabaseCategoryWithProducts>

    @Query("DELETE FROM categories")
    fun deleteAll()
}