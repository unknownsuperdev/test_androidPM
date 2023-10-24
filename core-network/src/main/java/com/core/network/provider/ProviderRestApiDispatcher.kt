package com.core.network.provider

import com.core.network.rest.dispatcher.RestApiDispatcher
import com.core.network.rest.header.Header
import kotlin.reflect.KClass

interface ProviderRestApiDispatcher {

    fun <REST : Any> provideRestApiDispatcher(kClass: KClass<REST>,
                                              domains: MutableList<String>,
                                              headers: List<Header>, scalarResponse: Boolean = false): RestApiDispatcher<REST>

}