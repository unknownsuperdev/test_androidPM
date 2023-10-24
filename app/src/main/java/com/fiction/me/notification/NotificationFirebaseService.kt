package com.fiction.me.notification

import android.util.Log
import com.fiction.domain.interactors.GetPushNotIdFromLocalDbUseCase
import com.fiction.domain.interactors.SetPushNotIdToLocalDbUseCase
import com.fiction.domain.interactors.SetPushTokenUseCase
import com.fiction.me.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NotificationFirebaseService : FirebaseMessagingService() {
    private val notification by inject<Notifications>()
    private val pushParser by inject<PushParser>()
    private val setPushTokenUseCase by inject<SetPushTokenUseCase>()
    private val setNotId by inject<SetPushNotIdToLocalDbUseCase>()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("FCMmessage", "onMessageReceivedData: ${remoteMessage.data}")
        val parseData = pushParser.parse(remoteMessage)
        CoroutineScope(IO).launch {
            setNotId(parseData.pushId)
            notification.notify(
                remoteMessage.notification?.title,
                remoteMessage.notification?.body ?: "",
                parseData,
                parseData.pushId,
                R.drawable.icon_push
            )
        }
    }

    override fun onNewToken(token: String) {
        CoroutineScope(IO).launch {
            delay(1300)
            setPushTokenUseCase(token)
        }
    }
}
