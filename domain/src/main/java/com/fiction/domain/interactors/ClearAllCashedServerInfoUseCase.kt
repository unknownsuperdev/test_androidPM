package com.fiction.domain.interactors


interface ClearAllCashedServerInfoUseCase {
    suspend operator fun invoke()
}