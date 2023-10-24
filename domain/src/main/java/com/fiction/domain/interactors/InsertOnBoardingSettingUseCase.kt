package com.fiction.domain.interactors

import com.fiction.domain.model.onboarding.OnBoardingSettingData

interface InsertOnBoardingSettingUseCase {
    suspend operator fun invoke(onBoardingSetting: OnBoardingSettingData)
}
