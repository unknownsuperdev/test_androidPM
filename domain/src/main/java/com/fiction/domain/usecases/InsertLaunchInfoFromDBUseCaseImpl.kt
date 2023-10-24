package com.fiction.domain.usecases

import com.fiction.domain.interactors.InsertLaunchInfoFromDBUseCase
import com.fiction.domain.repository.LaunchInfoRepo
import com.fiction.entities.roommodels.LaunchInfoEntity
import com.fiction.entities.utils.Constants

class InsertLaunchInfoFromDBUseCaseImpl(
    private val launchInfoRepo: LaunchInfoRepo
) : InsertLaunchInfoFromDBUseCase {

    override suspend fun invoke() {
        val isInstall = launchInfoRepo.getIsInstall(Constants.LAUNCH_INFO_STATUS_ID)
        if (isInstall == null)
            launchInfoRepo.insertData(LaunchInfoEntity(Constants.LAUNCH_INFO_STATUS_ID, false))
    }
}