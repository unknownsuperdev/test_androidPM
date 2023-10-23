package ru.tripster.domain.interactors.сalendar

interface SetExperienceIdUseCase {
    suspend operator fun invoke(
        experienceId: Int
    )
}