package ru.tripster.domain.interactors.notifications

interface GetRemindLaterTimeUseCase {

    suspend operator fun invoke(): String

}