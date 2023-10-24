package com.fiction.domain.interactors

import kotlinx.coroutines.flow.Flow

interface GetPushTokenFromLocalUseCase {
    suspend operator fun invoke(): Flow<String?>
}