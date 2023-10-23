package ru.tripster.domain.usecases.confirm

import com.google.gson.Gson
import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.confirmOrEdit.ChangeExperienceUseCase
import ru.tripster.domain.model.order.ChangeExperience
import ru.tripster.domain.model.order.ChangeExperience.Companion.from
import ru.tripster.domain.model.order.TicketType
import ru.tripster.domain.repository.events.OrderConfirmOrEditRepository
import ru.tripster.entities.request.confirm.TicketRequest

class ChangeExperienceUseCaseImpl(
    private val orderConfirmOrEditRepository: OrderConfirmOrEditRepository,
    private val dispatcher: CoroutineDispatcherProvider,
) :
    ChangeExperienceUseCase {
    override suspend fun invoke(
        experienceId: Int,
        personsCount: Int,
        date: String,
        tickets: List<TicketType>?,
        customPrice: Double?,
        time:String?
    ): ActionResult<ChangeExperience> = withContext(dispatcher.io) {
        val gson = Gson()
        val jsonStringTickets = gson.toJson(tickets)

        when (val result = orderConfirmOrEditRepository.changeExperience(
            experienceId,
            personsCount,
            date,
            if (!tickets.isNullOrEmpty()) jsonStringTickets else null,
            customPrice,
            time
        )) {
            is ActionResult.Success -> {
                ActionResult.Success(result.result.from())
            }

            is ActionResult.Error -> {
                ActionResult.Error(result.errors)
            }

        }
    }
}
