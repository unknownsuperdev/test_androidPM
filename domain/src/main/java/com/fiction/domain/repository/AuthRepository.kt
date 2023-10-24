package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.reader.ResponseMessage
import com.fiction.entities.response.registration.PushToken
import com.fiction.entities.response.registration.TokenResponse
import com.fiction.entities.response.registration.forgotpassword.*
import com.fiction.entities.response.registration.verification.TokenRequest
import com.fiction.entities.response.updatedevice.UpdateDeviceRequest

interface AuthRepository {
   /* suspend fun getGuestToken(
        uuId: String,
        isInstall: Boolean
    ): ActionResult<BaseResultModel<TokenResponse>>*/

    suspend fun updateDevice(
        updateDeviceRequest: UpdateDeviceRequest,
    ): ActionResult<BaseResultModel<ResponseMessage>>

    suspend fun setPushToken(pushToken: PushToken): ActionResult<BaseResultModel<ResponseMessage>>

    suspend fun getRegisterToken(
        uuId: String,
        email: String,
        password: String
    ): ActionResult<BaseResultModel<TokenResponse>>

    suspend fun getLoginToken(
        uuId: String,
        email: String,
        password: String
    ): ActionResult<BaseResultModel<TokenResponse>>

    suspend fun loginWithFacebook(
        uuId: String,
        identifier: String
    ): ActionResult<BaseResultModel<TokenResponse>>

    suspend fun loginWithGoogle(
        uuId: String,
        identifier: String
    ): ActionResult<BaseResultModel<TokenResponse>>

    suspend fun loginWithApple(
        uuId: String,
        identifier: String
    ): ActionResult<BaseResultModel<TokenResponse>>

    suspend fun logout(): ActionResult<BaseResultModel<ResponseMessage>>

    suspend fun forgotPassword(
        forgotPasswordRequest: ForgotPasswordRequest
    ): ActionResult<BaseResultModel<ForgotPasswordResponse>>

    suspend fun checkResetCode(
        checkResetCodeRequest: CheckResetCodeRequest
    ): ActionResult<BaseResultModel<CheckResetCodeResponse>>

    suspend fun resetPassword(
        resetPasswordRequest: ResetPasswordRequest
    ): ActionResult<BaseResultModel<MessageResponse>>

    suspend fun resendEmail(
        token: TokenRequest
    ): ActionResult<BaseResultModel<MessageResponse>>
}