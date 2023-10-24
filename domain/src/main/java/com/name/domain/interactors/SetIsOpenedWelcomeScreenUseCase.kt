package com.name.domain.interactors

interface SetIsOpenedWelcomeScreenUseCase {
    suspend operator fun invoke(isOpened: Boolean)
}