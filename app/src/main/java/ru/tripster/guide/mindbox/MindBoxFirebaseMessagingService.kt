package ru.tripster.guide.mindbox

import cloud.mindbox.mobile_sdk.Mindbox
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.tripster.guide.R
import ru.tripster.guide.ui.MainActivity

class MindBoxFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        // Передача токена в Mindbox SDK
        Mindbox.updatePushToken(applicationContext, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Активити поумолчанию. Откроется, если пришла ссылка, которой нет в перечислении
        val defaultActivity = MainActivity::class.java

        val channelId = "CHANNEL_ID"      // "my_android_app_channel"
        val channelName = "CHANNEL_NAME"         // "Рекламные рассылки"
        val channelDescription = "CHANNEL_DESC"  // "Рассылки, которые содержат рекламу"
        val pushSmallIcon = R.mipmap.ic_launcher

        // Перечисление ссылок и активити, которые должны открываться по разным ссылкам
        val activities = mapOf(
            "https://mindbox.ru/" to defaultActivity,
        )

        // Метод возвращает boolen, чтобы можно было сделать фолбек для обработки push-уведомлений
        Mindbox.handleRemoteMessage(
            context = applicationContext,
            message = remoteMessage,
            activities = activities,
            channelId = channelId, // Идентификатор канала для уведомлений, отправленных из Mindbox
            channelName = channelName,
            pushSmallIcon = pushSmallIcon, // Маленькая иконка для уведомлений
            defaultActivity = defaultActivity,
            channelDescription = channelDescription
        )

//        if (!messageWasHandled) {
//            // Если push-уведомление было не от Mindbox или в нем некорректные данные, то можно написать фолбек для его обработки
//        }
    }
}