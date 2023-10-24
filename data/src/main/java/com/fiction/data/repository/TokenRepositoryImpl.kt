package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.TokenService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.TokenRepository
import com.fiction.entities.request.launch.LaunchRequest
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.registration.TokenResponse

class TokenRepositoryImpl(
    private val authService: TokenService
) : TokenRepository {

    override suspend fun getGuestToken(
        uuId: String,
        isInstall: Boolean
    ): ActionResult<BaseResultModel<TokenResponse>> {
       return makeApiCall {
            analyzeResponse(
                authService.getGuestToken(
                    LaunchRequest(
                        udid = uuId,
                        isInstall = isInstall
                    )
                )
            )
        }
    }
}