package ru.tripster.domain.interactors.profile

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.profile.UserInfo


interface GetUserProfileInfoUseCase {
    suspend operator fun invoke(): ActionResult<UserInfo>
}