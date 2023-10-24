package com.fiction.domain.interactors

interface SetIsAppLaunchFirstTimeUseCase {
    suspend operator fun invoke(isOpened: Boolean)
}