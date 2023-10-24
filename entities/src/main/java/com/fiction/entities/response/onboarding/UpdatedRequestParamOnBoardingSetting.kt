package com.fiction.entities.response.onboarding

import com.google.gson.annotations.SerializedName

data class UpdatedRequestParamOnBoardingSetting(
    @SerializedName("gender_id")
    val genderId : Int? = null,
    @SerializedName("reading_time_id")
    val readingTimeId : Int? = null,
    @SerializedName("tag_ids")
    val tagIds : List<Long>? = null
)
