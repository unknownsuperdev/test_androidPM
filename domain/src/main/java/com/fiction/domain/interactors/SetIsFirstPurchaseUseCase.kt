package com.fiction.domain.interactors

interface SetIsFirstPurchaseUseCase {
    suspend operator fun invoke(isFirst: Boolean)
}