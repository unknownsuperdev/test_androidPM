package com.fiction.domain.usecases

import com.fiction.domain.interactors.DeleteOnBoardingSettingUseCase
import com.fiction.domain.repository.OnBoardingSettingRepo

class DeleteOnBoardingSettingUseCaseImpl(
    private val onBoardingSettingRepo: OnBoardingSettingRepo
) : DeleteOnBoardingSettingUseCase {

    override suspend fun invoke() {
        onBoardingSettingRepo.deleteTable()
    }
}
