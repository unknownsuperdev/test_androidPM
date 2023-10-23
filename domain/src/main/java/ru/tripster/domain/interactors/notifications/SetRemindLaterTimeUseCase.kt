package ru.tripster.domain.interactors.notifications

interface SetRemindLaterTimeUseCase {

    suspend operator fun invoke(time: String)

}