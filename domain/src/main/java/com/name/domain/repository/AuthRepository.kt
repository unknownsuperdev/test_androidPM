package com.name.domain.repository

import com.name.core.ActionResult
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.registration.GuestTokenResponse

interface AuthRepository {
    suspend fun getGuestToken(uuId: String): ActionResult<BaseResultModel<GuestTokenResponse>>
}