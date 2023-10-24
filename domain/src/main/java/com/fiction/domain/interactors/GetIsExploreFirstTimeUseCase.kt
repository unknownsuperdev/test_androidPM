package com.fiction.domain.interactors

import kotlinx.coroutines.flow.Flow

interface GetIsExploreFirstTimeUseCase {

    suspend operator fun invoke(): Flow<Boolean?>
}