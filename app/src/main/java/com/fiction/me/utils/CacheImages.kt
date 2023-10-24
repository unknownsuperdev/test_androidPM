package com.fiction.me.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

fun cacheImages(
    context: Context,
    imageUrl: String
) {
    val rm: RequestManager = Glide.with(context)
    rm.load(imageUrl).submit()
}