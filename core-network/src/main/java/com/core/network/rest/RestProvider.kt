package com.core.network.rest

import com.core.network.rest.header.Header
import kotlin.reflect.KClass

internal interface RestProvider {
    fun <T : Any> create(baseUrl: String, kClass: KClass<T>, headers: List<Header>, scalarResponse: Boolean): T

    fun <T : Any> create(baseUrl: String, kClass: KClass<T>): T
}