package xyz.goshanchik.prodavayka.data.domain

import xyz.goshanchik.prodavayka.util.round

data class Product(
    val id: Long,
    val name: String,
    val price: Float,
    val discount: Float,
    val description: String,
    val categoryId: Int,
    val pictureUrl: String,
) {

    val hasDiscount = discount > 0f

    val fullPrice: Float
        get() =
            if(hasDiscount)
                (price - ((price / 100f) * discount)).round(2).toFloat()
            else
                price.round(2).toFloat()
}