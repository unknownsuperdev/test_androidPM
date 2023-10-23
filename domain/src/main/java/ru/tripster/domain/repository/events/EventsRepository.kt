package ru.tripster.domain.repository.events

import androidx.paging.Pager
import ru.tripster.core.ActionResult
import ru.tripster.domain.model.events.EventsCallSortingModel
import ru.tripster.entities.response.events.ResultModel

interface EventsRepository {
    suspend fun getEvents(param: EventsCallSortingModel): Pager<Int, ResultModel>
    suspend fun getUnreadCountEvents(param: EventsCallSortingModel): Pager<Int, ResultModel>
    suspend fun getDateEvents(param: EventsCallSortingModel): Pager<Int, ResultModel>
    suspend fun getDateEventsLte(param: EventsCallSortingModel): Pager<Int, ResultModel>
    suspend fun getStatusSortingEvents(param: EventsCallSortingModel): Pager<Int, ResultModel>
    suspend fun getStatusDoubleSortingEvents(param: EventsCallSortingModel): Pager<Int, ResultModel>
    suspend fun getOrderEvent(orderId: Int): ActionResult<ResultModel>
    suspend fun getEventsDateCurrentGte(
        groupOrderId: Int,
        guideLastVisitDate: String
    ): ActionResult<ResultModel>

    suspend fun setCurrentAvailablePlaces(
        groupOrderId: Int,
        availablePlaces: Int,
        lastrVisitDate: String
    ): ActionResult<ResultModel>
}