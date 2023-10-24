package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.UpdateDevice

interface UpdateDeviceUseCase {
    suspend operator fun invoke(updateDevice: UpdateDevice): ActionResult<String>
}
