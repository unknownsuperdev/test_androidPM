package com.fiction.me.extensions

import kotlin.math.roundToInt

fun Double.roundOffDecimal(): Double =
    (((this * 1000.0).roundToInt() / 1000.0) * 100.0).roundToInt() / 100.0