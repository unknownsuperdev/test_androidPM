package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.registration.TokenResponse

interface TokenRepository {

    suspend fun getGuestToken(uuId: String, isInstall: Boolean): ActionResult<BaseResultModel<TokenResponse>>
}