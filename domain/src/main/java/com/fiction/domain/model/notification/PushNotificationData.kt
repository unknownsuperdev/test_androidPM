package com.fiction.domain.model.notification

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PushNotificationData(
    @SerializedName("title")
    val title: String?,
    @SerializedName("body")
    val body: String?,
    @SerializedName("type")
    val type: NotificationType?,
    @SerializedName("book_id")
    val bookId: Long?,
    @SerializedName("challenge_data")
    val challengeData: ChallengeData? = null,
    @SerializedName("sent_at")
    val sentAt: String?,
    @SerializedName("push_id")
    val pushId: Int,
    val timeInMillis: Long
) : Serializable
