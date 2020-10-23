package xyz.goshanchik.prodavayka.data.domain

data class CategoryWithProducts(
    val category: Category,
    val products: List<Product>
)