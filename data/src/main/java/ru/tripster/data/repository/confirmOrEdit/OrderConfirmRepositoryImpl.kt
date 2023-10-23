package ru.tripster.data.repository.confirmOrEdit

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.confirmOrEdit.OrderConfirmOrEditService
import ru.tripster.data.util.analyzeConfirmEditResponse
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.events.OrderConfirmOrEditRepository
import ru.tripster.entities.request.confirm.OrderConfirmRequest
import ru.tripster.entities.request.confirm.TicketRequest
import ru.tripster.entities.response.experience.ChangeExperienceResponse
import ru.tripster.entities.response.orders.OrderDetailsResponse

class OrderConfirmRepositoryImpl(private val orderConfirmService: OrderConfirmOrEditService) :
    OrderConfirmOrEditRepository {
    override suspend fun confirmOrder(
        id: Int,
        confirmOrder: OrderConfirmRequest
    ): ActionResult<OrderDetailsResponse> = makeApiCall {
        analyzeConfirmEditResponse(orderConfirmService.confirmOrder(id, confirmOrder))
    }

    override suspend fun editOrder(
        id: Int,
        confirmOrder: OrderConfirmRequest
    ): ActionResult<OrderDetailsResponse> = makeApiCall {
        analyzeConfirmEditResponse(orderConfirmService.editOrders(id, confirmOrder))
    }

    override suspend fun changeExperience(
        experienceId: Int,
        personsCount: Int,
        date: String,
        tickets: String?,
        customPrice: Double?,
        time:String?
    ): ActionResult<ChangeExperienceResponse> = makeApiCall {
        analyzeResponse(
            orderConfirmService.changeExperience(
                experienceId,
                personsCount,
                date,
                tickets,
                customPrice,
                time
            )
        )
    }
}