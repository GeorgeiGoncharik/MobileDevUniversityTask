package xyz.goshanchik.prodavayka.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
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

fun NetworkProductContainer.asDatabaseModel(): List<DatabaseProduct> {
    return products.map {

        val discount = when(val raw = (it.discount.toFloat() % 100).toInt()){
            in 70..100 -> 0
            else -> raw
        }
        DatabaseProduct(
            id = it.id.toLong(),
            name = it.name,
            price = it.price.toFloat(),
            discount = discount.toFloat(),
            description = it.description,
            categoryId = it.categoryId.toInt(),
            pictureUrl = it.pictureUrl
        )
    }
}