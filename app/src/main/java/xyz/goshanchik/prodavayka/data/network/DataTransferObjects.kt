package xyz.goshanchik.prodavayka.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.OffsetDateTime
import xyz.goshanchik.prodavayka.data.database.DatabaseCategory
import xyz.goshanchik.prodavayka.data.database.DatabaseProduct

@JsonClass(generateAdapter = true)
data class NetworkCategoryContainer(val categories: List<NetworkCategory>)

@JsonClass(generateAdapter = true)
data class NetworkProductContainer(val products: List<NetworkProduct>)

@JsonClass(generateAdapter = true)
data class NetworkCategory(
    val id: String,
    val name: String,
    val description: String,
    @Json(name="picture_url")
    val pictureUrl: String
)

@JsonClass(generateAdapter = true)
data class NetworkProduct(
    val id: String,
    @Json(name="CategoryId")
    val categoryId: String,
    val name: String,
    val price: String,
    val discount: String,
    val description: String,
    @Json(name="picture_url")
    val pictureUrl: String
)

fun NetworkCategory.asDatabaseModel(): DatabaseCategory =
    DatabaseCategory(
        id = this.id.toInt(),
        name = this.name,
        description = this.description,
        pictureUrl = this.pictureUrl
    )

fun NetworkCategoryContainer.asDatabaseModel(): List<DatabaseCategory> {
    return categories.map{
        DatabaseCategory(
            id = it.id.toInt(),
            name = it.name,
            description = it.description,
            pictureUrl = it.pictureUrl
        )
    }
}

fun NetworkProduct.asDatabaseModel(recent: OffsetDateTime? = null): DatabaseProduct {
    val discount = when(val raw = (this.discount.toFloat() % 100).toInt()){
        in 70..101 -> 0
        else -> raw
    }.toFloat()

    return DatabaseProduct(
        id = this.id.toLong(),
        name = this.name,
        price = this.price.toFloat(),
        discount = discount,
        description = this.description,
        categoryId = this.categoryId.toInt(),
        pictureUrl = this.pictureUrl,
        recent = recent
    )
}


fun NetworkProductContainer.asDatabaseModel(): List<DatabaseProduct> {
    return products.map {

        val discount = when(val raw = (it.discount.toFloat() % 100).toInt()){
            in 70..101 -> 0
            else -> raw
        }.toFloat()

        DatabaseProduct(
            id = it.id.toLong(),
            name = it.name,
            price = it.price.toFloat(),
            discount = discount,
            description = it.description,
            categoryId = it.categoryId.toInt(),
            pictureUrl = it.pictureUrl
        )
    }
}