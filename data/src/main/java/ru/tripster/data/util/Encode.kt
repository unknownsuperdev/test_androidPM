package ru.tripster.data.util

import android.util.Base64

fun encodeLoginPass(username:String, password:String):String {
    val authPayload = "$username:$password"
    val data = authPayload.toByteArray()
    val base64 = Base64.encodeToString(data, Base64.NO_WRAP)
    val token = "Basic $base64"
    return token
}