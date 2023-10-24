package com.fiction.me.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.fiction.me.R

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

fun Int.toFormatViewsLikes(): String {

    val formattedCount = when {
        this >= 1000000 -> {
            val firstDigit = this / 1000000
            val afterDotDigit = (this % 1000000) / 100000
            if (afterDotDigit != 0) "$firstDigit.${afterDotDigit}kk" else "${firstDigit}kk"
        }
        this >= 1000 -> {
            val firstDigit = this / 1000
            val afterDotDigit = (this % 1000) / 100
            if (afterDotDigit != 0) "$firstDigit.${afterDotDigit}k" else "${firstDigit}k"
        }
        this >= 0 -> {
            this.toString()
        }
        else -> "0"
    }
    return formattedCount
}