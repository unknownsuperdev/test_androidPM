package com.core.network.provider

import com.core.network.di.module
import com.core.network.rest.dispatcher.RestApiDispatcher
import com.core.network.rest.dispatcher.RestApiDispatcherImpl
import com.core.network.rest.header.Header
import org.koin.core.context.loadKoinModules
import kotlin.reflect.KClass

class ProviderRestApiDispatcherImpl : ProviderRestApiDispatcher {

    init {
        loadKoinModules(module)
    }

    override fun <REST : Any> provideRestApiDispatcher(
        kClass: KClass<REST>,
        domains: MutableList<String>,
        headers: List<Header>,
        scalarResponse: Boolean
    ): RestApiDispatcher<REST> {
        return RestApiDispatcherImpl(kClass, domains, headers, scalarResponse)
    }

}