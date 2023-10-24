package com.name.domain.interactors

import com.name.core.ActionResult
import com.name.domain.model.profile.ProfileInfo

interface GetProfileInfoUseCase {
    suspend operator fun invoke(): ActionResult<ProfileInfo>
}