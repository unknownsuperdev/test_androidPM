package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.ProfileService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.model.profile.UpdateRequestParam
import com.fiction.domain.repository.ProfileRepo
import com.fiction.entities.response.profile.ProfileInfoResponse
import com.fiction.entities.response.BaseResultModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ProfileRepoImpl(private val profileService: ProfileService) : ProfileRepo {

    override suspend fun getProfileInfo(): ActionResult<BaseResultModel<ProfileInfoResponse>> =
        makeApiCall {
            analyzeResponse(profileService.getProfileInfo())
        }

    override suspend fun updateProfile(
        name: String?,
        avatarUri: String?,
        autoUnlockPaid: Boolean?,
        readingMode: Boolean?
    ): ActionResult<BaseResultModel<Any>> {
        val fileReqBody =
            if (avatarUri.isNullOrEmpty()) null else File(avatarUri).asRequestBody("image/*".toMediaType())
        return makeApiCall {
            analyzeResponse(
                profileService.updateProfile(
                    UpdateRequestParam(
                        name,
                        fileReqBody,
                        autoUnlockPaid,
                        readingMode
                    )
                )
            )
        }
    }

}
