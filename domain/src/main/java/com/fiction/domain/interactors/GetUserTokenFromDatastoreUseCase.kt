package com.fiction.domain.interactors

interface GetUserTokenFromDatastoreUseCase {
    suspend operator fun invoke(): String?
}