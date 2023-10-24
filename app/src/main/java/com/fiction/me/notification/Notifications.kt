package com.fiction.me.notification

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fiction.me.R
import com.fiction.me.utils.Constants.Companion.APP_NAME
import java.io.Serializable

interface Notifications {

    fun notify(
        title: String?,
        message: String,
        serializeObject: Serializable? = null,
        id: Int,
        @DrawableRes icon: Int
    )

    fun notify(@StringRes title: Int, message: String, id: Int)

    fun notify(@StringRes title: Int, id: Int)

    fun notify(title: String? = APP_NAME, message: String, id: Int) =
        notify(title, message, null, id, R.drawable.ic_push_logo)

    fun notify(title: String?, message: String, id: Int, serializeObject: Serializable) =
        notify(title, message, serializeObject, id, R.drawable.ic_push_logo)
}