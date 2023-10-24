package com.name.domain.interactors

import kotlinx.coroutines.flow.Flow

interface GetIsOpenedWelcomeScreenUseCase {
    suspend operator fun invoke(): Flow<Boolean?>
}