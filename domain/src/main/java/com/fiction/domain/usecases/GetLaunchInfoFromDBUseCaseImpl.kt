package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetLaunchInfoFromDBUseCase
import com.fiction.domain.repository.LaunchInfoRepo
import com.fiction.entities.utils.Constants.Companion.LAUNCH_INFO_STATUS_ID

class GetLaunchInfoFromDBUseCaseImpl(
    private val launchInfoRepo: LaunchInfoRepo
) : GetLaunchInfoFromDBUseCase {

    override suspend fun invoke(): Boolean? {
        val isInstall = launchInfoRepo.getIsInstall(LAUNCH_INFO_STATUS_ID)
      /*  if (isInstall == true)
            launchInfoRepo.insertData(LaunchInfoEntity(LAUNCH_INFO_STATUS_ID, false))*/
        return isInstall
    }
}