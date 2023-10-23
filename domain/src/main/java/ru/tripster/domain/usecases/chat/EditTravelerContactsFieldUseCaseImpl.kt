package ru.tripster.domain.usecases.chat

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.chat.EditTravelerContactsFieldUseCase
import ru.tripster.domain.model.confirmOrEdit.EditTravelerContactsOpen
import ru.tripster.domain.model.confirmOrEdit.EditTravelerContactsOpen.Companion.from
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.domain.model.order.OrderDetails.Companion.from
import ru.tripster.domain.repository.order.OrderDetailsRepository

class EditTravelerContactsFieldUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val orderDetailsRepository: OrderDetailsRepository
) : EditTravelerContactsFieldUseCase {
    override suspend fun invoke(
        id: Int,
        travelerContactsOpen: EditTravelerContactsOpen
    ): ActionResult<OrderDetails> = withContext(dispatcher.io) {
        when (val result = orderDetailsRepository.editOrder(id, travelerContactsOpen.from())) {
            is ActionResult.Success -> {
                result.result.let {
                    ActionResult.Success(it.from())
                }
            }
            is ActionResult.Error -> {
                ActionResult.Error(result.errors)
            }
        }
    }
}