package com.fiction.domain.model

import com.fiction.domain.model.enums.FeedTypes

data class SuggestedBookParams(
    val feedTypes: FeedTypes,
    val genreId: Int = -1
)
