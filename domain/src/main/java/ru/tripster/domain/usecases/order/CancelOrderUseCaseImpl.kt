package ru.tripster.domain.usecases.order

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.order.CancelOrderUseCase
import ru.tripster.domain.model.cancel.OrderCancelData
import ru.tripster.domain.model.cancel.OrderCancelData.Companion.from
import ru.tripster.domain.repository.order.OrderDetailsRepository

class CancelOrderUseCaseImpl(
    private val orderDetails: OrderDetailsRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : CancelOrderUseCase {
    override suspend fun invoke(id: Int, cancelOrder: OrderCancelData): ActionResult<Boolean> =
        withContext(dispatcher.io) {
            when (val apiData = orderDetails.cancelOrder(id, cancelOrder.from())) {
                is ActionResult.Success -> {
                    ActionResult.Success(true)
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}