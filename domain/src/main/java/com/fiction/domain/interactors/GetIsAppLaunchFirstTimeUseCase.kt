package com.fiction.domain.interactors

import kotlinx.coroutines.flow.Flow

interface GetIsAppLaunchFirstTimeUseCase {
    suspend operator fun invoke(): Flow<Boolean?>
}