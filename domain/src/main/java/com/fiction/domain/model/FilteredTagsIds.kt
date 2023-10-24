package com.fiction.domain.model

import com.google.gson.annotations.SerializedName

data class FilteredTagsIds(
    @SerializedName("tag_ids")
    val tagIds: List<Long>
)
