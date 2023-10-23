package ru.tripster.data.repository.events

import androidx.paging.Pager
import androidx.paging.PagingConfig
import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.events.EventsApiService
import ru.tripster.data.dataservice.pagingSource.events.*
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.model.events.EventsCallSortingModel
import ru.tripster.domain.repository.events.EventsRepository
import ru.tripster.entities.responce.response.events.NumberOfPersonsAvailRequest
import ru.tripster.entities.response.events.ResultModel

class EventsRepositoryImpl(
    private val eventsApiService: EventsApiService
) : EventsRepository {
    override suspend fun getEvents(
        param: EventsCallSortingModel
    ): Pager<Int, ResultModel> =
        Pager(
            PagingConfig(
                param.pageSize,
                enablePlaceholders = false,
                prefetchDistance = param.pageSize,
                initialLoadSize = param.pageSize
            )
        ) {
            EventsPagingSource.Factory().create(eventsApiService)
        }

    override suspend fun getUnreadCountEvents(param: EventsCallSortingModel): Pager<Int, ResultModel> =
        Pager(
            PagingConfig(
                param.pageSize,
                enablePlaceholders = false,
                prefetchDistance = param.pageSize,
                initialLoadSize = param.pageSize
            )
        ) {
            EventsUnreadPagingSource.Factory().create(eventsApiService)
        }

    override suspend fun getDateEvents(param: EventsCallSortingModel): Pager<Int, ResultModel> =
        Pager(
            PagingConfig(
                param.pageSize,
                enablePlaceholders = false,
                prefetchDistance = param.pageSize,
                initialLoadSize = param.pageSize
            )
        ) {
            EventsDatePagingSource.Factory().create(
                eventsApiService,
                param.status.number,
                param.sorting,
                param.startDate
            )
        }

    override suspend fun getDateEventsLte(param: EventsCallSortingModel): Pager<Int, ResultModel> =
        Pager(
            PagingConfig(
                param.pageSize,
                enablePlaceholders = false,
                prefetchDistance = param.pageSize,
                initialLoadSize = param.pageSize
            )
        ) {
            EventsDatePagingSourceLte.Factory().create(
                eventsApiService,
                param.status.number,
                param.sorting,
                param.startDate
            )
        }

    override suspend fun getStatusSortingEvents(param: EventsCallSortingModel): Pager<Int, ResultModel> =
        Pager(
            PagingConfig(
                param.pageSize,
                enablePlaceholders = false,
                prefetchDistance = param.pageSize,
                initialLoadSize = param.pageSize
            )
        ) {
            EventsStatusSortingPagingSource.Factory()
                .create(eventsApiService, param.status.number, param.sorting)
        }

    override suspend fun getStatusDoubleSortingEvents(param: EventsCallSortingModel): Pager<Int, ResultModel> =

        Pager(
            PagingConfig(
                param.pageSize,
                enablePlaceholders = false,
                prefetchDistance = param.pageSize,
                initialLoadSize = param.pageSize
            )
        ) {
            EventsDoubleStatusSortingPagingSource.Factory()
                .create(eventsApiService, param.status.number - 1, param.status.number, param.sorting)
        }

    override suspend fun getEventsDateCurrentGte(
        groupOrderId: Int,
        guideLastVisitDate: String
    ): ActionResult<ResultModel> =
        makeApiCall {
            analyzeResponse(
                eventsApiService.getEventsDateCurrentGte(
                    groupOrderId, NumberOfPersonsAvailRequest(null, guideLastVisitDate)
                )
            )
        }

    override suspend fun getOrderEvent(orderId: Int): ActionResult<ResultModel> = makeApiCall {
        analyzeResponse(eventsApiService.getOrderEvent(orderId))
    }

    override suspend fun setCurrentAvailablePlaces(
        groupOrderId: Int,
        availablePlaces: Int,
        lastrVisitDate: String
    ): ActionResult<ResultModel> {
        return makeApiCall {
            analyzeResponse(
                eventsApiService.setEventAvailablePlaces(
                    id = groupOrderId,
                    numberOfPersonsAvail = NumberOfPersonsAvailRequest(
                        guideLastVisitDate = lastrVisitDate,
                        numberOfPersonsAvail = availablePlaces,
                    )
                )
            )
        }
    }
}