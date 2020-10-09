package xyz.goshanchik.prodavayka.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithProducts(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val products: List<Product>
)