package com.fiction.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings


class Uuid(private val context: Context) {
    //var newUuid: String? = null

    @SuppressLint("HardwareIds")
    fun getUuid(): String {
        return /*newUuid ?:*/ Settings.Secure.getString(context.contentResolver,
            Settings.Secure.ANDROID_ID)
    }

}
