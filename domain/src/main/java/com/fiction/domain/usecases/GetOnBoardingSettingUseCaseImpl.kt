package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetOnBoardingSettingUseCase
import com.fiction.domain.model.onboarding.OnBoardingSettingData
import com.fiction.domain.repository.OnBoardingSettingRepo

class GetOnBoardingSettingUseCaseImpl(
    private val onBoardingSettingRepo: OnBoardingSettingRepo
) : GetOnBoardingSettingUseCase {

    override suspend fun invoke(): OnBoardingSettingData =
        onBoardingSettingRepo.getOnBoardingSettingsData()?.let {
            OnBoardingSettingData.from(it)
        } ?: OnBoardingSettingData.emptyItem()

}
