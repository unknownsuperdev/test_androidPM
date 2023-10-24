package com.fiction.domain.interactors

import kotlinx.coroutines.flow.Flow

interface GetIsDataRestoredUseCase {
    suspend operator fun invoke(): Flow<Boolean?>
}