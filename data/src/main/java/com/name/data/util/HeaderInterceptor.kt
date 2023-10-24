package com.name.data.util


import com.name.data.dataservice.appservice.DataStoreService
import com.name.data.util.Constants.Companion.HEADER_PARAM_GUEST_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(private val dataStoreService: DataStoreService) : Interceptor {
    lateinit var request: Request
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .method(originalRequest.method, originalRequest.body)

        request = if (originalRequest.url.toString().contains("api/v1/launch")) {
            requestBuilder.build()
        } else {
            /*   CoroutineScope(Dispatchers.IO).launch {
                val userToken = dataStoreService.getUserToken()
                userToken.collect { userToken ->
                    if (!userToken.isNullOrEmpty()) {
                        requestBuilder.header(HEADER_PARAM_USER_TOKEN, userToken)
                    } else {
                        dataStoreService.getGuestToken().collect {
                            if (it.isNullOrEmpty())
                                requestBuilder.header(HEADER_PARAM_GUEST_TOKEN, "Bearer $it")
                        }
                    }
                }
            }*/
            requestBuilder.header(
                HEADER_PARAM_GUEST_TOKEN,
                "Bearer 1689|0Uew09nAn2cuN7fJkrLyGswGb3VIyKZomVADUZVHT4jZ9Lb8EqhkA8ObqkwU"
            )
            requestBuilder.build()
        }
        return chain.proceed(request)
    }
}