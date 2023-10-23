package ru.tripster.domain.interactors.order

import kotlinx.coroutines.flow.Flow

interface GetMenuItemUseCase {
    suspend operator fun invoke() : Flow<String?>

}