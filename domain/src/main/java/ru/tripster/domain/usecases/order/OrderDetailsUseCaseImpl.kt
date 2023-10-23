package ru.tripster.domain.usecases.order

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.order.OrderDetailsUseCase
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.domain.model.order.OrderDetails.Companion.from
import ru.tripster.domain.repository.order.OrderDetailsRepository

class OrderDetailsUseCaseImpl(
    private val orderDetails: OrderDetailsRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : OrderDetailsUseCase {
    override suspend fun invoke(id: Int): ActionResult<OrderDetails> = withContext(dispatcher.io) {
        when (val apiData = orderDetails.getOrders(id)) {
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
