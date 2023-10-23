package ru.tripster.domain.usecases.confirm

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.confirmOrEdit.OrderConfirmOrEditUseCase
import ru.tripster.domain.model.confirmOrEdit.OrderConfirmOrEditData
import ru.tripster.domain.model.confirmOrEdit.OrderConfirmOrEditData.Companion.from
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.domain.model.order.OrderDetails.Companion.from
import ru.tripster.domain.repository.events.OrderConfirmOrEditRepository

class OrderConfirmUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val orderConfirmRepository: OrderConfirmOrEditRepository
) : OrderConfirmOrEditUseCase {
    override suspend fun invoke(
        id: Int,
        confirmOrder: OrderConfirmOrEditData, isConfirm: Boolean
    ): ActionResult<OrderDetails> = withContext(dispatcher.io) {
        when (val apiData = if (isConfirm) {
            orderConfirmRepository.confirmOrder(id, confirmOrder.from())
        } else {
            orderConfirmRepository.editOrder(id, confirmOrder.from())
        }) {
            is ActionResult.Success -> {
                apiData.result.let {
                    ActionResult.Success(it.from())
                }
            }

            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }

}