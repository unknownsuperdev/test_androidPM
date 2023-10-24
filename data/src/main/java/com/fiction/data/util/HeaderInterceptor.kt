package com.fiction.data.util

import com.fiction.core.ActionResult
import com.fiction.core.utils.Uuid
import com.fiction.data.dataservice.apiservice.TokenService
import com.fiction.data.dataservice.appservice.PreferencesService
import com.fiction.domain.repository.LaunchInfoRepo
import com.fiction.entities.request.launch.LaunchRequest
import com.fiction.entities.utils.Constants.Companion.LAUNCH_INFO_STATUS_ID
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.closeQuietly
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.atomic.AtomicBoolean

class HeaderInterceptor(private val dataStoreService: PreferencesService) : Interceptor,
    KoinComponent {

    private val tokenService: TokenService by inject()
    private val launchInfoRepo: LaunchInfoRepo by inject()
    private val uuid: Uuid by inject()
    private var updateToken = AtomicBoolean(false)
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .method(originalRequest.method, originalRequest.body)
        val token = dataStoreService.getToken()

        val request = requestBuilder.run {
            header(Constants.HEADER_PARAM_TOKEN, "Bearer $token")
            requestBuilder.build()
        }
        var response = chain.proceed(request)

        if (response.code == 401) {
            runBlocking {
                mutex.withLock {
                    if (updateToken.get()) {
                        response = chain.proceed(
                            repeatRequest(
                                originalRequest,
                                response,
                                dataStoreService.getToken()
                            )
                        )
                    } else {
                        val isInstall =
                            launchInfoRepo.getIsInstall(LAUNCH_INFO_STATUS_ID) ?: true
                        val refreshToken = dataStoreService.getRefreshToken()
                        val refreshTokenRequest = analyzeResponse(
                            tokenService.updateToken("Bearer $refreshToken")
                        )
                        when (refreshTokenRequest) {
                            is ActionResult.Success -> {
                                val newToken = refreshTokenRequest.result.data
                                updateToken.set(true)
                                dataStoreService.run {
                                    newToken?.accessToken?.let { setToken(it) }
                                    newToken?.refreshToken?.let { setRefreshToken(it) }
                                }
                                response = chain.proceed(
                                    repeatRequest(
                                        originalRequest,
                                        response,
                                        newToken?.accessToken
                                    )
                                )
                            }
                            is ActionResult.Error -> {
                                val requestResult = makeApiCall {
                                    analyzeResponse(
                                        tokenService.getGuestToken(
                                            LaunchRequest(
                                                udid = uuid.getUuid(),
                                                isInstall = isInstall
                                            )
                                        )
                                    )
                                }
                                (requestResult as? ActionResult.Success)?.let { result ->
                                    val newToken = result.result.data
                                    dataStoreService.run {
                                        newToken?.let {
                                            setToken(it.accessToken ?: "")
                                            setRefreshToken(it.refreshToken ?: "")
                                        }
                                    }
                                    updateToken.set(true)
                                    response = chain.proceed(
                                        repeatRequest(
                                            originalRequest,
                                            response,
                                            newToken?.accessToken
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (!mutex.isLocked) {
                updateToken.set(false)
            }
        }
        return response
    }

    private fun repeatRequest(request: Request, response: Response, token: String?): Request {
        response.closeQuietly()
        return request.newBuilder()
            .header(
                Constants.HEADER_PARAM_TOKEN,
                "Bearer $token"
            )
            .build()
    }
}
