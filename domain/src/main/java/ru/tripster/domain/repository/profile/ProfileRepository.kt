package ru.tripster.domain.repository.profile

import ru.tripster.core.ActionResult
import ru.tripster.entities.response.profile.UserInfoResponse

interface ProfileRepository {
    suspend fun getUserInfo(): ActionResult<UserInfoResponse>
    suspend fun deleteAccount(): ActionResult<Unit>
}