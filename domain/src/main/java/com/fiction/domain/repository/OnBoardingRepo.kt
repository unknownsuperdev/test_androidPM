package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.domain.model.onboarding.BooksForYouResponseItem
import com.fiction.entities.response.onboarding.OnBoardingSettingResponse
import com.fiction.entities.response.onboarding.UpdatedRequestParamOnBoardingSetting
import com.fiction.entities.response.BaseResultModel

interface OnBoardingRepo {

    suspend fun getOnBoardingSetting(): ActionResult<BaseResultModel<OnBoardingSettingResponse>>

    suspend fun getBooksForYou(): ActionResult<BaseResultModel<List<BooksForYouResponseItem>>>

    suspend fun updateOnBoarding(
        updatedParam: UpdatedRequestParamOnBoardingSetting
    ): ActionResult<BaseResultModel<String>>
}
