package com.rocappdev.beerscatalog.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.rocappdev.beerscatalog.R

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun loadImage(image: ImageView, url: String) {
    Glide
        .with(image.context)
        .load(url)
        .error(image.context.getDrawable(R.drawable.placeholder))
        .into(image)
}