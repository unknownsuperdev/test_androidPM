package com.fiction.me.extention

import android.text.TextUtils
import java.util.regex.Pattern

fun String.isValidEmail(): Boolean =
    !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = Pattern.compile(
    "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).*\$"
).matcher(this).matches() && this.length > 5