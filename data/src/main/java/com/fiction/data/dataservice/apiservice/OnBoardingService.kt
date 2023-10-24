package com.fiction.data.dataservice.apiservice

import com.fiction.domain.model.onboarding.BooksForYouResponseItem
import com.fiction.entities.response.onboarding.OnBoardingSettingResponse
import com.fiction.entities.response.onboarding.UpdatedRequestParamOnBoardingSetting
import com.fiction.entities.response.BaseResultModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OnBoardingService {

    @GET("v1/onboarding/settings")
    suspend fun getOnBoardingSetting(): Response<BaseResultModel<OnBoardingSettingResponse>>

    @GET("v1/onboarding/books/for-you")
    suspend fun getBooksForYou(): Response<BaseResultModel<List<BooksForYouResponseItem>>>

    @POST("v1/onboarding/update")
    suspend fun updateOnBoarding(
        @Body updatedParam: UpdatedRequestParamOnBoardingSetting
    ): Response<BaseResultModel<String>>

}
