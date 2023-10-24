package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.analytic.AnalyticFacebook
import org.json.JSONObject

interface SetFacebookDataUseCase {
    suspend operator fun invoke(analyticFacebook: AnalyticFacebook, webInfo: JSONObject?): ActionResult<String>
}