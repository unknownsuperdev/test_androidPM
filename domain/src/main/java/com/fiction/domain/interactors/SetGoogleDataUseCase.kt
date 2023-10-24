package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import org.json.JSONObject

interface SetGoogleDataUseCase {
    suspend operator fun invoke(analyticGoogle: JSONObject): ActionResult<String>
}