package com.fiction.data.dataservice.apiservice

import com.fiction.domain.model.profile.UpdateRequestParam
import com.fiction.entities.response.profile.ProfileInfoResponse
import com.fiction.entities.response.BaseResultModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileService {

    @GET("v1/user/profile")
    suspend fun getProfileInfo(): Response<BaseResultModel<ProfileInfoResponse>>

    @POST("v1/user/profile/update")
    suspend fun updateProfile(
        @Body profInfo: UpdateRequestParam
    ): Response<BaseResultModel<Any>>

}