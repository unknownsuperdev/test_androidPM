package com.fiction.domain.interactors

import com.fiction.domain.model.onboarding.OnBoardingSettingData

interface GetOnBoardingSettingUseCase {
    suspend operator fun invoke() : OnBoardingSettingData
}
