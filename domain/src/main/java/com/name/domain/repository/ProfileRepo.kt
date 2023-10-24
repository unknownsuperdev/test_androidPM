package com.name.domain.repository

import com.name.core.ActionResult
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.profile.ProfileInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileRepo {
    suspend fun getProfileInfo(): ActionResult<BaseResultModel<ProfileInfoResponse>>

    suspend fun updateProfile(
        name: String?,
        avatarUri: String?,
        autoUnlockPaid: Boolean?,
        readingMode: Boolean?
    ): ActionResult<BaseResultModel<Any>>

    suspend fun removeProfileAvatar(): ActionResult<BaseResultModel<Any>>

}