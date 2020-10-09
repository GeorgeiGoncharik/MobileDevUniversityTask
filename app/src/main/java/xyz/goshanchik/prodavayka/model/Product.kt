package xyz.goshanchik.prodavayka.model

import androidx.room.*

@Entity(tableName = "Products",
    foreignKeys = [
        ForeignKey(entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE
        )],
    indices = [
        Index(value = ["category_id"])]
)
class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val price: Float,
    val discount: Float = 0f,
    val description: String = "",
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "picture_url") val pictureUrl: String?,
) {


    @Ignore
    val hasDiscount = discount > 0f


    val fullPrice: Float
        get() =
            if(hasDiscount)
                price - ((price / 100f) * discount)
            else
                price
}