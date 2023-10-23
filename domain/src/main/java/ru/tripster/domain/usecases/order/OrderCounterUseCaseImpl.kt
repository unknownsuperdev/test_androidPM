package ru.tripster.domain.usecases.order

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.order.OrderCounterUseCase
import ru.tripster.domain.model.order.OrderCounterModel
import ru.tripster.domain.repository.order.OrderDetailsRepository

class OrderCounterUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val orderDetailsRepository: OrderDetailsRepository
) : OrderCounterUseCase {
    override suspend fun invoke(): ActionResult<OrderCounterModel> = withContext(dispatcher.io) {
        when (val apiData = orderDetailsRepository.getCounters()) {
            is ActionResult.Success -> {
                apiData.result.let {
                    ActionResult.Success(OrderCounterModel.from(it))
                }
            }
            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}
