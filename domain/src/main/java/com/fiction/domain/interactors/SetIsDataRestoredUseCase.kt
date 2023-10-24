package com.fiction.domain.interactors

interface SetIsDataRestoredUseCase {
    suspend operator fun invoke(isRestored: Boolean)
}