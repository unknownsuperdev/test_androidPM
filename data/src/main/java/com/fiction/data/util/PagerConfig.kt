package com.fiction.data.util

import androidx.paging.PagingConfig
import com.fiction.data.util.Constants.Companion.DEFAULT_PAGE_SIZE

fun getPagerConfig(pageSize: Int = DEFAULT_PAGE_SIZE) =
    PagingConfig(
        pageSize,
        enablePlaceholders = false,
        prefetchDistance = pageSize,
        initialLoadSize = pageSize
    )

