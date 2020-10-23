package xyz.goshanchik.prodavayka.util

import android.content.Context
import kotlin.math.round
import kotlin.math.roundToInt

fun Float.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun dpToPx(context: Context ,dp: Int): Int {
    val density: Float = context.resources.displayMetrics.density
    return (dp.toFloat() * density).roundToInt()
}