package com.fiction.me.notification

import androidx.core.text.isDigitsOnly
import com.fiction.domain.model.notification.ChallengeData
import com.fiction.domain.model.notification.NotificationType.Companion.toNotificationType
import com.fiction.domain.model.notification.PushNotificationData
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class PushParser {

    fun parse(remoteMessage: RemoteMessage) = remoteMessage.data.run {
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val type = get("type").toString()
        val bookId = get("book_id").toString()
        val pushId = get("push_id").toString().toInt()
        val challengeData =
            Gson().fromJson(get("challenge_data").toString(), ChallengeData::class.java)
        val sendAt = get("sent_at").orEmpty()

        PushNotificationData(
            title = title,
            body = body,
            type = type.toNotificationType(),
            bookId = if (bookId.isDigitsOnly() && bookId.isNotEmpty()) bookId.toLong() else null,
            challengeData = challengeData,
            sentAt = sendAt,
            pushId = pushId,
            timeInMillis = System.currentTimeMillis()
        )
    }
}