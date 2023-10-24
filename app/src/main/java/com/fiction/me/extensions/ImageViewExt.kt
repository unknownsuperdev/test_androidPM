package com.fiction.me.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.loadUrl(
    uri: String,
    context: Context,
    rondSize: Int = 0
) {
    if (rondSize == 0)
        Glide.with(context)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val loadedFromCache = dataSource == DataSource.DATA_DISK_CACHE
                    Log.i("GlideImageAni", "disk = $loadedFromCache, memory = ${dataSource == DataSource.MEMORY_CACHE}" +
                            ", resource = ${dataSource == DataSource.RESOURCE_DISK_CACHE}, locale = ${dataSource == DataSource.LOCAL}" +
                            ", remote = ${dataSource == DataSource.REMOTE}")
                    return false                        }

            })
            .into(this)
    else
        Glide.with(context)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .transform(RoundedCorners(context.dpToPx(rondSize)))
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val loadedFromCache = dataSource == DataSource.DATA_DISK_CACHE
                    Log.i("GlideImageAni", "disk = $loadedFromCache, memory = ${dataSource == DataSource.MEMORY_CACHE}" +
                            ", resource = ${dataSource == DataSource.RESOURCE_DISK_CACHE}, locale = ${dataSource == DataSource.LOCAL}" +
                            ", remote = ${dataSource == DataSource.REMOTE}")
                    return false                        }

            })
            .into(this)

}
