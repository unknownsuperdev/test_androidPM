package ru.tripster.domain.interactors.order

import kotlinx.coroutines.flow.Flow

interface GetGuideEmailUseCase {
    suspend operator fun invoke() : Flow<String?>

}