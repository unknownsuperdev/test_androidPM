package com.name.jat.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Int.correctColor(ctx: Context, @ColorRes color: Int) =
    if (this != -1)
        this
    else
        ContextCompat.getColor(ctx, color)


fun Int.dpToPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
}

val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()