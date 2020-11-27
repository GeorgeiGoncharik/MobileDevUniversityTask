package xyz.goshanchik.prodavayka.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.goshanchik.prodavayka.data.database.DatabaseCartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cart WHERE product_id=:productId")
    suspend fun getCartItemByProductId(productId: Long): DatabaseCartItem

    @Query("SELECT * FROM cart WHERE cart_id=:id")
    suspend fun getCartItemById(id: Long): DatabaseCartItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCartItem(databaseCartItem: DatabaseCartItem)

    @Update
    suspend fun updateCartItem(databaseCartItem: DatabaseCartItem)

    @Query("DELETE FROM cart WHERE cart_id=:id")
    suspend fun deleteCartItem(id: Long)
}