package com.fiction.data.repository

import android.content.Context
import com.bumptech.glide.Glide
import com.fiction.domain.repository.CacheImageRepo

class CacheImageRepoImpl(private val context: Context) : CacheImageRepo {

    override suspend fun setCache(url: String) {
        Glide.with(context).load(url).submit()
    }

}