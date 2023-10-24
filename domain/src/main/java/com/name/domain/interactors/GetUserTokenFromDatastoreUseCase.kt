package com.name.domain.interactors

import kotlinx.coroutines.flow.Flow

interface GetUserTokenFromDatastoreUseCase {
    suspend operator fun invoke(): Flow<String?>
}