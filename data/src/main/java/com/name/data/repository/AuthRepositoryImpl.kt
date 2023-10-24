package com.name.data.repository

import com.name.core.ActionResult
import com.name.data.dataservice.apiservice.AuthService
import com.name.data.util.analyzeResponse
import com.name.data.util.makeApiCall
import com.name.domain.repository.AuthRepository
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.registration.GuestTokenResponse

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {

    override suspend fun getGuestToken(uuId: String): ActionResult<BaseResultModel<GuestTokenResponse>> =
        makeApiCall {
            analyzeResponse(
                authService.getGuestToken(
                    udid = uuId
                )
            )
        }
}