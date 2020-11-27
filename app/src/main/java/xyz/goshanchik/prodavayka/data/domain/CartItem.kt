package xyz.goshanchik.prodavayka.data.domain

data class CartItem(
    val id: Long,
    val product: Product,
    val category: Category,
    val quantity: Int
) {
}