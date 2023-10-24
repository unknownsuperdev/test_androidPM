package com.fiction.domain.model.notification

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChallengeData(
    @SerializedName("name")
    val name: String,
    @SerializedName("item")
    val item: String
): Serializable
