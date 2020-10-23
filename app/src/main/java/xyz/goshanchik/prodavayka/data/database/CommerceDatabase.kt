package xyz.goshanchik.prodavayka.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.goshanchik.prodavayka.data.database.dao.CartDao
import xyz.goshanchik.prodavayka.data.database.dao.CategoryDao
import xyz.goshanchik.prodavayka.data.database.dao.ProductDao
import xyz.goshanchik.prodavayka.data.domain.Category
import xyz.goshanchik.prodavayka.data.domain.Product

@Database(entities = [DatabaseCategory::class, DatabaseProduct::class], version = 1, exportSchema = false)
abstract class CommerceRoomDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun productDao(): ProductDao

    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: CommerceRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CommerceRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CommerceRoomDatabase::class.java,
                    "commerce_database"
                )
                    //.addCallback(CommerceDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }

    private class CommerceDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {

                    val categoryDao = database.categoryDao()
                    val productDao = database.productDao()

                    categoryDao.deleteAll()

                    val phones = DatabaseCategory(
                        name = "Mobile Phones",
                        description = "",
                        pictureUrl = "https://cdn.pocket-lint.com/r/s/1200x630/assets/images/120309-phones-buyer-s-guide-best-smartphones-2020-the-top-mobile-phones-available-to-buy-today-image1-eagx1ykift.jpg"
                    )

                    val laptops = DatabaseCategory(
                        id = 1000,
                        name = "Laptops",
                        description = "",
                        pictureUrl = "https://cdn.mos.cms.futurecdn.net/X5TyA8uvkGXoNyjFzxcowS-1200-80.jpg"
                    )

                    val laptopProduct = DatabaseProduct(
                        name = "Dell XPS 15",
                        price = 1305.09f,
                        description = "i7, 16gb RAM, 512gb SSD",
                        pictureUrl = "https://static.1k.by/images/products/ip/big/pp9/7/4174815/ic083cd029.jpeg",
                        categoryId = laptops.id
                    )

                    val laptopProduct1 = DatabaseProduct(
                        name = "Dell XPS 17",
                        price = 1305.09f,
                        description = "i7, 16gb RAM, 512gb SSD",
                        pictureUrl = "https://static.1k.by/images/products/ip/big/pp9/7/4174815/ic083cd029.jpeg",
                        categoryId = laptops.id
                    )

                    val parfume = DatabaseCategory(
                        name = "Parfume",
                        description = "",
                        pictureUrl = "https://png.pngtree.com/thumb_back/fw800/back_our/20190621/ourmid/pngtree-woman-perfume-literary-flower-pink-banner-image_181283.jpg"
                    )

                    categoryDao.insertCategories(phones, laptops, parfume)

                    productDao.insertProducts(laptopProduct, laptopProduct1)
                }
            }
        }
    }
}