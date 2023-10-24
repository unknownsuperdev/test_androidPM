package com.fiction.data.repository

import com.fiction.data.dataservice.apiservice.OnBoardingService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.OnBoardingRepo
import com.fiction.entities.response.onboarding.UpdatedRequestParamOnBoardingSetting

class OnBoardingRepoImpl(
    private val onBoardingService: OnBoardingService
) : OnBoardingRepo {

    override suspend fun getOnBoardingSetting() =
        makeApiCall {
            analyzeResponse(onBoardingService.getOnBoardingSetting())
        }

    override suspend fun getBooksForYou() =
        makeApiCall {
            analyzeResponse(onBoardingService.getBooksForYou())
        }

    override suspend fun updateOnBoarding(updatedParam: UpdatedRequestParamOnBoardingSetting) =
        makeApiCall {
            analyzeResponse(onBoardingService.updateOnBoarding(updatedParam))
        }
}
