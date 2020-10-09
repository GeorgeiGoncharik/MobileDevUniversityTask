package xyz.goshanchik.prodavayka.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.goshanchik.prodavayka.data.dao.CategoryDao
import xyz.goshanchik.prodavayka.data.dao.ProductDao
import xyz.goshanchik.prodavayka.model.Category
import xyz.goshanchik.prodavayka.model.Product

@Database(entities = [Category::class, Product::class], version = 2, exportSchema = false)
abstract class CommerceRoomDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun productDao(): ProductDao

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
                    .addCallback(WordDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {

                    val categoryDao = database.categoryDao()

                    categoryDao.deleteAll()

                    val phones = Category(
                        name = "Mobile Phones",
                        description = "",
                        pictureUrl = "https://cdn.pocket-lint.com/r/s/1200x630/assets/images/120309-phones-buyer-s-guide-best-smartphones-2020-the-top-mobile-phones-available-to-buy-today-image1-eagx1ykift.jpg"
                    )

                    val laptops = Category(
                        name = "Laptops",
                        description = "",
                        pictureUrl = "https://cdn.mos.cms.futurecdn.net/X5TyA8uvkGXoNyjFzxcowS-1200-80.jpg"
                    )

                    val parfume = Category(
                        name = "Parfume",
                        description = "",
                        pictureUrl = "https://png.pngtree.com/thumb_back/fw800/back_our/20190621/ourmid/pngtree-woman-perfume-literary-flower-pink-banner-image_181283.jpg"
                    )

                    categoryDao.insertCategories(phones, laptops, parfume)
                }
            }
        }
    }
}