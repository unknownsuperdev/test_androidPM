package com.core.network.rest.header

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class HeaderInterceptor(private val headers: List<Header>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestWithHeader = originalRequest.newBuilder()

        runBlocking {
            headers.forEach { list ->
                list.getHeaders().forEach { header ->
                    requestWithHeader.header(header.key, header.value)
                }
            }
        }

        val request = requestWithHeader.build()

        return chain.proceed(request)
    }

}