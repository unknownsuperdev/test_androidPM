package com.fiction.domain.interactors

import kotlinx.coroutines.flow.Flow

interface GetUuidUseCase {
    suspend operator fun invoke(): Flow<String?>
}