package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.GetFavoriteTagsUseCase
import com.fiction.domain.model.onboarding.FavoriteThemeTags
import com.fiction.domain.repository.OnBoardingRepo
import com.fiction.domain.repository.OnBoardingSettingRepo
import kotlinx.coroutines.withContext

class GetFavoriteTagsUseCaseImpl(
    private val onBoardingRepo: OnBoardingRepo,
    private val onBoardingSettingRepo: OnBoardingSettingRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : GetFavoriteTagsUseCase {

    override suspend fun invoke(): ActionResult<List<FavoriteThemeTags>> =
        withContext(dispatcher.io) {
            when (val apiData = onBoardingRepo.getOnBoardingSetting()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        val selectedTagList = onBoardingSettingRepo.getOnBoardingSettingsData()?.favoriteThemeTags
                        val favoriteThemeTags =
                            response.tags?.map { FavoriteThemeTags.from(it,selectedTagList) } ?: listOf()
                        ActionResult.Success(favoriteThemeTags)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
