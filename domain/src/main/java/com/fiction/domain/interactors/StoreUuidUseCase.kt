package com.fiction.domain.interactors

interface StoreUuidUseCase {
    suspend operator fun invoke(uuid: String)
}