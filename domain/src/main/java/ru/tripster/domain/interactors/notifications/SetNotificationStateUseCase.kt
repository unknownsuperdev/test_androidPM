package ru.tripster.domain.interactors.notifications

interface SetNotificationStateUseCase {

    suspend operator fun invoke (isSelected : Boolean)
}