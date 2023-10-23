package ru.tripster.domain.interactors.menuDebug

interface SaveCurrentStageUseCase {
    suspend fun invoke(stage: String)
}
