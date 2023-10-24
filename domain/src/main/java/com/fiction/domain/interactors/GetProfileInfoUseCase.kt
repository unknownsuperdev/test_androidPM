package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.profile.ProfileInfo

interface GetProfileInfoUseCase {
    suspend operator fun invoke(isMakeCallAnyWay: Boolean = false, isClear: Boolean = false): ActionResult<ProfileInfo>
    suspend fun updateBalance(balance: Int)
    fun updateReadingMode(readingMode: Boolean)
    fun updateAutoUnlockMode(autoUnlock: Boolean)
}