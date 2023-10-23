package ru.tripster.domain.usecases.order

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.order.GetRejectInfoUseCase
import ru.tripster.domain.model.order.orderrejectinfo.RejectInfo
import ru.tripster.domain.model.order.orderrejectinfo.RejectInfo.Companion.from
import ru.tripster.domain.repository.order.OrderDetailsRepository

class GetRejectInfoUseCaseImpl(
    private val orderDetails: OrderDetailsRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : GetRejectInfoUseCase {
    override suspend fun invoke(id: Int): ActionResult<RejectInfo> = withContext(dispatcher.io) {
        when (val apiData = orderDetails.getRejectInfo(id)) {
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