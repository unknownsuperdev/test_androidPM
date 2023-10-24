package com.fiction.me.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.fiction.me.R
import com.fiction.me.ui.MainActivity
import com.fiction.me.utils.Constants.Companion.NOTIFICATION_CHANEL
import com.fiction.me.utils.Constants.Companion.NOTIFICATION_CLICKED_ACTION
import com.fiction.me.utils.Constants.Companion.NOTIFICATION_REQUEST_CODE
import com.fiction.me.utils.Constants.Companion.PUSH_NOTIFICATION_EXTRA
import java.io.Serializable

class NotificationImpl(
    private val context: Context,
    private val notificationManager: NotificationManager
) : Notifications {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel = NotificationChannel(
                NOTIFICATION_CHANEL,
                NOTIFICATION_CHANEL,
                NotificationManager.IMPORTANCE_HIGH
            )
            chanel.enableVibration(true)
            notificationManager.createNotificationChannel(chanel)
        }
    }

    override fun notify(
        title: String?,
        message: String,
        serializeObject: Serializable?,
        id: Int,
        icon: Int
    ) {
        val pendingIntent = buildIntent(serializeObject)
        val notification = buildNotification(title, pendingIntent, message, icon)
        notificationManager.notify(id, notification)
    }

    override fun notify(title: Int, message: String, id: Int) =
        notify(context.resources.getString(title), message, id)

    override fun notify(title: Int, id: Int) = notify(context.resources.getString(title), "", id)

    private fun buildIntent(serializeObject: Serializable? = null) = Intent(
        NOTIFICATION_CLICKED_ACTION
    ).run {
        PendingIntent.getActivity(
            context,
            NOTIFICATION_REQUEST_CODE,
            getActivityIntent().putExtra(PUSH_NOTIFICATION_EXTRA, serializeObject),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

    private fun getActivityIntent(): Intent = Intent(context, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        action = Intent.ACTION_MAIN
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    private fun buildNotification(
        title: String?,
        pendingIntent: PendingIntent,
        message: String,
        icon: Int
    ) = NotificationCompat.Builder(context, NOTIFICATION_CHANEL)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setColor(
            ContextCompat.getColor(
                context,
                R.color.heliotrope
            )
        )
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setSmallIcon(icon)
        .build()
}