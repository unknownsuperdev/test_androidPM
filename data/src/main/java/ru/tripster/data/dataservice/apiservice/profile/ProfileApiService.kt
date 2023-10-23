package ru.tripster.data.dataservice.apiservice.profile

import androidx.room.Delete
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import ru.tripster.entities.response.profile.UserInfoResponse

interface ProfileApiService {
    @GET("guides/v2/info/profile/")
    suspend fun getUserInfo(): Response<UserInfoResponse>

    @DELETE("web/v1/users/me/")
    suspend fun deleteAccount(): Response<Unit>
}