package ru.tripster.domain.interactors.order

import kotlinx.coroutines.flow.Flow

interface GetGuideIdUseCase {
    suspend operator fun invoke() : Flow<Int?>

}