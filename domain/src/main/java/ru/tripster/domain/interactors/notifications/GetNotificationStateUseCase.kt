package ru.tripster.domain.interactors.notifications

interface GetNotificationStateUseCase {
    suspend operator fun invoke() : Boolean
}