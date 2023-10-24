package com.fiction.domain.interactors

import kotlinx.coroutines.flow.Flow

interface GetIsFirstPurchaseUseCase {
    suspend operator fun invoke(): Flow<Boolean?>
}