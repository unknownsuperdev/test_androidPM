package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.UpdateOnBoardingSettingUseCase
import com.fiction.domain.repository.OnBoardingRepo
import com.fiction.entities.response.onboarding.UpdatedRequestParamOnBoardingSetting
import kotlinx.coroutines.withContext

class UpdateOnBoardingSettingUseCaseImpl(
    private val onBoardingRepo: OnBoardingRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : UpdateOnBoardingSettingUseCase {

    override suspend fun invoke(
        genderId: Int?,
        readingTimeId: Int?,
        favoriteTagIds: List<Long>?
    ): ActionResult<String> =
        withContext(dispatcher.io) {
            when (val apiData = onBoardingRepo.updateOnBoarding(
                UpdatedRequestParamOnBoardingSetting(
                    genderId,
                    readingTimeId,
                    favoriteTagIds
                )
            )) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        ActionResult.Success(response)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }

}
