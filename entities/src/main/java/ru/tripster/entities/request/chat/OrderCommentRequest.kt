package ru.tripster.entities.request.chat

import com.google.gson.annotations.SerializedName

data class OrderCommentRequest(
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("post_contacts")
    val post_contacts: Boolean?,
    @SerializedName("user_role")
    val user_role: String?,
    @SerializedName("viewed_by_guide")
    val viewed_by_guide: Boolean?
)