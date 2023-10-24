package com.fiction.domain.usecases

import com.fiction.domain.interactors.InsertOnBoardingSettingUseCase
import com.fiction.domain.model.onboarding.OnBoardingSettingData
import com.fiction.domain.repository.OnBoardingSettingRepo

class InsertOnBoardingSettingUseCaseImpl(
    private val onBoardingSettingRepo: OnBoardingSettingRepo
) : InsertOnBoardingSettingUseCase {

    override suspend fun invoke(onBoardingSetting: OnBoardingSettingData) {
        onBoardingSettingRepo.setOnBoardingSettingsData(
            OnBoardingSettingData.toEntity(
                onBoardingSetting
            )
        )
    }
}
