package com.fiction.domain.repository

interface CacheImageRepo {

    suspend fun setCache(url: String)

}