package xyz.goshanchik.prodavayka.data.database

import androidx.room.*
import org.threeten.bp.OffsetDateTime
import xyz.goshanchik.prodavayka.data.domain.CartItem
import xyz.goshanchik.prodavayka.data.domain.Category
import xyz.goshanchik.prodavayka.data.domain.CategoryWithProducts
import xyz.goshanchik.prodavayka.data.domain.Product

@Entity(tableName = "categories")
data class DatabaseCategory(
    @PrimaryKey
    val id: Int = 0,
    val name: String ="",
    val description: String = "",
    @ColumnInfo(name = "picture_url") val pictureUrl: String = "",
)

@Entity(tableName = "products",
    foreignKeys = [
        ForeignKey(entity = DatabaseCategory::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE
        )],
    indices = [
        Index(value = ["category_id"])]
)
data class DatabaseProduct(
    @PrimaryKey
    val id: Long = 0L,
    val name: String,
    val price: Float,
    val discount: Float = 0f,
    val description: String = "",
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "picture_url") val pictureUrl: String,
    val recent: OffsetDateTime? = null
)

@Entity(tableName = "cart",
    foreignKeys = [
        ForeignKey(entity = DatabaseProduct::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("product_id"),
            onDelete = ForeignKey.CASCADE
        )],
    indices = [
        Index(value = ["product_id"])])
data class DatabaseCartItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cart_id")
    val id: Long = 0,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    var quantity: Int = 0
)

data class DatabaseCategoryWithProducts(
    @Embedded val category: DatabaseCategory,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val products: List<DatabaseProduct>
){
    fun asDomainModel(): CategoryWithProducts{
        return CategoryWithProducts(
            category = Category(
                id = this.category.id,
                name = this.category.name,
                description = this.category.description,
                pictureUrl = this.category.pictureUrl
            ),
            products = this.products.asDomainModel()
        )
    }
}

data class DomainCartItem(
    @Embedded val product: DatabaseProduct, // <-- here you get your id and quantity

    @Relation(
        parentColumn = "category_id",
        entityColumn = "id",
    )
    val category: DatabaseCategory,

    @Relation(
        parentColumn = "id",
        entityColumn = "product_id"
    )
    val cartItem: DatabaseCartItem
)

@JvmName("asDomainModelCategory")
fun List<DatabaseCategory>.asDomainModel(): List<Category> {
    return map{
        Category(
            id = it.id,
            name = it.name,
            description = it.description,
            pictureUrl = it.pictureUrl
        )
    }
}

@JvmName("asDomainModelProduct")
fun List<DatabaseProduct>.asDomainModel(): List<Product> {
    return map {
        Product(
            id = it.id,
            name = it.name,
            price = it.price,
            discount = it.discount,
            description = it.description,
            categoryId = it.categoryId,
            pictureUrl = it.pictureUrl
        )
    }
}

fun DatabaseCategory.asDomainModel(): Category = Category(id, name, description, pictureUrl)
fun DatabaseProduct.asDomainModel(): Product = Product(id, name, price, discount, description, categoryId, pictureUrl)