package xyz.goshanchik.prodavayka.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        GlideApp
            .with(view.context)
            .load(url)
            .into(view)
    }
}