package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.profile.ProfileInfoResponse
import com.fiction.entities.response.BaseResultModel

interface ProfileRepo {
    suspend fun getProfileInfo(): ActionResult<BaseResultModel<ProfileInfoResponse>>

    suspend fun updateProfile(
        name: String?,
        avatarUri: String?,
        autoUnlockPaid: Boolean?,
        readingMode: Boolean?
    ): ActionResult<BaseResultModel<Any>>

}