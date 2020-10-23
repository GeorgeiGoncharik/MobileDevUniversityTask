package xyz.goshanchik.prodavayka.data.database

import androidx.room.*
import xyz.goshanchik.prodavayka.data.domain.Category
import xyz.goshanchik.prodavayka.data.domain.CategoryWithProducts
import xyz.goshanchik.prodavayka.data.domain.Product

@Entity(tableName = "Categories")
data class DatabaseCategory(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val description: String = "",
    @ColumnInfo(name = "picture_url") val pictureUrl: String?,
)

@Entity(tableName = "Products",
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
    val recent: Int = 0
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
                pictureUrl = this.category.pictureUrl ?: "https://hellenic.property/wp-content/uploads/2018/01/No-Image.jpg"
            ),
            products = this.products.asDomainModel()
        )
    }
}

@JvmName("asDomainModelCategory")
fun List<DatabaseCategory>.asDomainModel(): List<Category> {
    return map{
        Category(
            id = it.id,
            name = it.name,
            description = it.description,
            pictureUrl = it.pictureUrl
                ?: "https://hellenic.property/wp-content/uploads/2018/01/No-Image.jpg"
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
                ?: "https://hellenic.property/wp-content/uploads/2018/01/No-Image.jpg"
        )
    }
}

fun DatabaseProduct.asDomainModel(): Product = Product(id, name, price, discount, description, categoryId, pictureUrl)