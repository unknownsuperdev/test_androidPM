package com.fiction.domain.utils

import android.util.Log
import kotlin.properties.Delegates

object NetworkStatus {
    var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
        Log.i("NetworkStatus",newValue.toString())
    }
}