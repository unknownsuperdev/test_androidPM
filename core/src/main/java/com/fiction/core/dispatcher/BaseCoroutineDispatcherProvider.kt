package com.fiction.core.dispatcher

import kotlinx.coroutines.Dispatchers

class BaseCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    override val main by lazy { Dispatchers.Main }
    override val io by lazy { Dispatchers.IO }
}