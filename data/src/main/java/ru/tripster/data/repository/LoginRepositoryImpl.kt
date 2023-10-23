package ru.tripster.data.repository

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.AuthService
import ru.tripster.data.util.analyzeAuthResponse
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.LoginRepository
import ru.tripster.entities.request.LoginRequest
import ru.tripster.entities.response.*

class LoginRepositoryImpl(private val authService: AuthService) : LoginRepository {

    override suspend fun login(loginRequest: LoginRequest): ActionResult<LoginResponse> {
        return makeApiCall {
            analyzeAuthResponse(authService.login(loginRequest))
        }
    }
}