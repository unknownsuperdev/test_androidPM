package ru.tripster.data.dataservice.apiservice

import ru.tripster.entities.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.tripster.entities.request.LoginRequest

interface AuthService {
    @POST("web/v1/auth/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

}
