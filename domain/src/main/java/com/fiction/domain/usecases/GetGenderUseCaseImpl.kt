package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.GetGenderUseCase
import com.fiction.domain.model.onboarding.Gender
import com.fiction.domain.repository.OnBoardingRepo
import kotlinx.coroutines.withContext

class GetGenderUseCaseImpl(
    private val onBoardingRepo: OnBoardingRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : GetGenderUseCase {

    override suspend fun invoke(): ActionResult<List<Gender>> =
        withContext(dispatcher.io) {
            when (val apiData = onBoardingRepo.getOnBoardingSetting()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        val genders = response.gender?.map { Gender.from(it) } ?: listOf()
                        ActionResult.Success(genders)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
