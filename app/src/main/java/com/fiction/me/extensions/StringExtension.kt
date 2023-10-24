package com.fiction.me.extensions

import android.text.TextUtils
import com.fiction.domain.model.BookInfo
import com.fiction.me.utils.ModelType
import com.google.gson.Gson
import java.util.regex.Pattern

fun String.isValidEmail(): Boolean =
    !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = Pattern.compile(
    "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).*\$"
).matcher(this).matches()

fun String.fromGson(model: ModelType): Any = when(model){
    ModelType.BOOK_INFO -> Gson().fromJson(this, BookInfo::class.java)
}

