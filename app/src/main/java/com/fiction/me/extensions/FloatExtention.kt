package com.fiction.me.extensions

import android.content.Context
import android.util.TypedValue

fun Float.dpToPx(context: Context): Float {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
}
