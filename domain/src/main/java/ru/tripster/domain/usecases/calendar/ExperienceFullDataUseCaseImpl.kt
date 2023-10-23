package ru.tripster.domain.usecases.calendar


import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.сalendar.ExperienceFullDataUseCase
import ru.tripster.domain.model.calendar.filtering.Experience
import ru.tripster.domain.repository.сalendar.ExperienceRepository

class ExperienceFullDataUseCaseImpl(private val experienceRepository: ExperienceRepository) :
    ExperienceFullDataUseCase {
    override suspend fun invoke(isVisible: Boolean): ActionResult<Experience> {
       return  when (val result = experienceRepository.getExperienceFullData(isVisible)){
            is ActionResult.Success -> {
                 ActionResult.Success(Experience.from(result.result))
            }

            is ActionResult.Error -> {
                ActionResult.Error(result.errors)
            }
        }
    }
}