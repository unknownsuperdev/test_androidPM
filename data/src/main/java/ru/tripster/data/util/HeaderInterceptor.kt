package ru.tripster.data.util

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ru.tripster.data.dataservice.appservice.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.component.KoinComponent
import ru.tripster.data.util.Constants.Companion.ENABLE_DETAILED_MOBILE_ERRORS
import ru.tripster.data.util.Constants.Companion.HEADER_PARAM_COOKIE
import ru.tripster.data.util.Constants.Companion.HEADER_PARAM_USER_TOKEN

class HeaderInterceptor(
    private val dataStoreService: DataStoreService
) : Interceptor, KoinComponent {
    lateinit var request: Request

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val deviceID = runBlocking {
            dataStoreService.getDeviceId().first()
        }
        val userToken = runBlocking {
            dataStoreService.getUserToken().first()
        }
        val requestBuilder = originalRequest.newBuilder()
            .method(originalRequest.method, originalRequest.body)

        request = when {
            originalRequest.url.toString().contains("v1/auth/login/") -> {
                requestBuilder.header(
                    HEADER_PARAM_USER_TOKEN,
                    "Basic ZXhwZXJpZW5jZTp0cmlwc3Rlcl9kZXY="
                )
                requestBuilder.header(
                    HEADER_PARAM_COOKIE,
                    "device_id=${deviceID}"
                )
                requestBuilder.header(
                    ENABLE_DETAILED_MOBILE_ERRORS,
                    ""
                )
                requestBuilder.build()
            }

            else ->  {
                requestBuilder.header(
                    HEADER_PARAM_USER_TOKEN,
                    "Basic ZXhwZXJpZW5jZTp0cmlwc3Rlcl9kZXY="
                )
                requestBuilder.header(
                    Constants.HEADER_PARAM_API_AUTHORIZATION,
                    "Token $userToken"
                )
                requestBuilder.header(
                    Constants.HEADER_PARAM_COOKIE,
                    "device_id=${deviceID}"
                )
                requestBuilder.build()
            }
        }
        return chain.proceed(request)
    }

}