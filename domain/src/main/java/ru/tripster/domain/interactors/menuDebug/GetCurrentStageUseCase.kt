package ru.tripster.domain.interactors.menuDebug

import kotlinx.coroutines.flow.Flow

interface GetCurrentStageUseCase {
    suspend operator fun invoke() : Flow<String?>
}
