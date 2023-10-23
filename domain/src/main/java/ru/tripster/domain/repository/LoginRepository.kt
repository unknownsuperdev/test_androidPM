package ru.tripster.domain.repository

import ru.tripster.core.ActionResult
import ru.tripster.entities.request.LoginRequest
import ru.tripster.entities.response.*

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): ActionResult<LoginResponse>
}