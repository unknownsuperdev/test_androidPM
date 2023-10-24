package com.name.data.dataservice.apiservice

import com.name.domain.model.profile.UpdateRequestParam
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.profile.ProfileInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileService {

    @GET("/api/v1/user/profile")
    suspend fun getProfileInfo(): Response<BaseResultModel<ProfileInfoResponse>>


    @POST("/api/v1/user/profile/update")
    suspend fun updateProfile(
        @Body profInfo: UpdateRequestParam
    ): Response<BaseResultModel<Any>>

    @POST("/api/v1/user/profile/avatar/remove")
    suspend fun removeProfileAvatar(): Response<BaseResultModel<Any>>
}