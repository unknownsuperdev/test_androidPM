package com.core.network.rest.dispatcher

import com.core.network.rest.header.Header
import com.core.network.rest.response.model.ApiResponse
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass

abstract class RestApiDispatcher<REST : Any>(
    protected val kClass: KClass<REST>,
    protected var domains: MutableList<String>,
    protected val headers: List<Header>,
    protected val scalarResponse: Boolean
) : KoinComponent {

    abstract suspend fun <T> executeRequest(header: String? = null, block: suspend (restApi: REST) -> ApiResponse<T>): ApiResponse<T>

    abstract suspend fun setDomain(domain: List<String>)

    abstract  fun getDomain(): String

}