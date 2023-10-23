package ru.tripster.guide.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import ru.tripster.domain.model.MenuItems
import ru.tripster.domain.model.order.TotalPriceAndPercent

fun Int.correctColor(ctx: Context, @ColorRes color: Int) =
    if (this != -1)
        this
    else
        ContextCompat.getColor(ctx, color)

fun Int.countPercent(percent: Int): TotalPriceAndPercent {
    val forYou = this.minus(this * percent / 100)
    val onApp = this.minus(forYou)

    return TotalPriceAndPercent(this, onApp, forYou)
}

fun Int.availablePlacesRightForm(): String {

    return when {
        this in 11..20 -> "мест"
        this.toString().takeLast(1).toInt() == 1 -> "место"
        this.toString().takeLast(1).toInt() in 2..4 -> "места"
        else -> "мест"
    }
}

fun Int.menuItem(): String {
    return when (this) {
        0 -> MenuItems.ORDERS.type
        1 -> MenuItems.CALENDAR.type
        else -> MenuItems.PROFILE.type
    }
}
fun Int.pxToDp(context: Context): Float {
    return this / context.resources.displayMetrics.density
}