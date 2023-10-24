package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import org.json.JSONObject

interface SetTiktokDataUseCase {
    suspend operator fun invoke(analyticTiktok: JSONObject): ActionResult<String>
}