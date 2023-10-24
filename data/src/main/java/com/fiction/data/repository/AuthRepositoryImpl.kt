package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.AuthService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.AuthRepository
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.reader.ResponseMessage
import com.fiction.entities.response.registration.PushToken
import com.fiction.entities.response.registration.TokenResponse
import com.fiction.entities.response.registration.forgotpassword.*
import com.fiction.entities.response.registration.verification.TokenRequest
import com.fiction.entities.response.updatedevice.UpdateDeviceRequest

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {
   /* override suspend fun getGuestToken(
        uuId: String,
        isInstall: Boolean
    ): ActionResult<BaseResultModel<TokenResponse>> {
        TODO("Not yet implemented")
    }*/

    override suspend fun updateDevice(
        updateDeviceRequest: UpdateDeviceRequest,
    ): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(
                authService.updateDevice(
                    updateDeviceRequest
                )
            )
        }

    override suspend fun setPushToken(pushToken: PushToken): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(
                authService.setPushToken(
                    pushToken
                )
            )
        }

    override suspend fun getRegisterToken(
        uuId: String,
        email: String,
        password: String
    ): ActionResult<BaseResultModel<TokenResponse>> =
        makeApiCall {
            analyzeResponse(
                authService.getRegisterToken(
                    udid = uuId,
                    email = email,
                    password = password
                )
            )
        }

    override suspend fun getLoginToken(
        uuId: String,
        email: String,
        password: String
    ): ActionResult<BaseResultModel<TokenResponse>> =
        makeApiCall {
            analyzeResponse(
                authService.getLoginToken(
                    udid = uuId,
                    email = email,
                    password = password
                )
            )
        }

    override suspend fun loginWithFacebook(
        uuId: String,
        identifier: String
    ): ActionResult<BaseResultModel<TokenResponse>> =
        makeApiCall {
            analyzeResponse(
                authService.loginWithFacebook(
                    udid = uuId,
                    identifier = identifier
                )
            )
        }

    override suspend fun loginWithGoogle(
        uuId: String,
        identifier: String
    ): ActionResult<BaseResultModel<TokenResponse>> =
        makeApiCall {
            analyzeResponse(
                authService.loginWithGoogle(
                    udid = uuId,
                    identifier = identifier
                )
            )
        }

    override suspend fun loginWithApple(
        uuId: String,
        identifier: String
    ): ActionResult<BaseResultModel<TokenResponse>> =
        makeApiCall {
            analyzeResponse(
                authService.loginWithApple(
                    udid = uuId,
                    identifier = identifier
                )
            )
        }

    override suspend fun logout(): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(
                authService.logout()
            )
        }

    override suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) =
        makeApiCall {
            analyzeResponse(
                authService.forgotPassword(forgotPasswordRequest)
            )
        }

    override suspend fun checkResetCode(checkResetCodeRequest: CheckResetCodeRequest) =
        makeApiCall {
            analyzeResponse(
                authService.checkResetCode(checkResetCodeRequest)
            )
        }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest) =
        makeApiCall {
            analyzeResponse(
                authService.resetPassword(resetPasswordRequest)
            )
        }

    override suspend fun resendEmail(token: TokenRequest) =
        makeApiCall {
            analyzeResponse(
                authService.resendEmail(token)
            )
        }
}