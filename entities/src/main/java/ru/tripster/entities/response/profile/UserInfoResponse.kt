package ru.tripster.entities.response.profile

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("avatar150_url")
    val avatarImage: String?,
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    val id: Int?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("exp_count_published")
    val expCountPublished: Int
)
