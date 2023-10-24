package com.name.data.repository

import com.name.core.ActionResult
import com.name.data.dataservice.apiservice.ProfileService
import com.name.data.util.analyzeResponse
import com.name.data.util.makeApiCall
import com.name.domain.model.profile.UpdateRequestParam
import com.name.domain.repository.ProfileRepo
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.profile.ProfileInfoResponse
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

    override suspend fun removeProfileAvatar(): ActionResult<BaseResultModel<Any>> =
        makeApiCall {
            analyzeResponse(profileService.removeProfileAvatar())
        }

}
