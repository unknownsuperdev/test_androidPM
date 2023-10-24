package com.name.jat.extensions

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Context.dpToPx(id: Int): Int = resources.getDimensionPixelSize(id)

fun <T> debounce(
    scope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(300)
            destinationFunction(param)
        }
    }
}

fun Context.getCurrentCacheSize() = cacheDir?.walkBottomUp()
    ?.fold(0L) { acc, file ->
    Log.e("@err", "$acc  - ${file.length()}")
        acc + file.length()
    }