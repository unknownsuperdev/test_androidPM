package ru.tripster.data.repository.profile

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.profile.ProfileApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.profile.ProfileRepository
import ru.tripster.entities.response.profile.UserInfoResponse

class ProfileRepositoryImpl(
    var profileApiService: ProfileApiService
):ProfileRepository {
    override suspend fun getUserInfo(): ActionResult<UserInfoResponse> =
        makeApiCall {
            analyzeResponse(
                profileApiService.getUserInfo()
            )
        }

    override suspend fun deleteAccount(): ActionResult<Unit> = makeApiCall {
        analyzeResponse(
            profileApiService.deleteAccount()
        )
    }
}