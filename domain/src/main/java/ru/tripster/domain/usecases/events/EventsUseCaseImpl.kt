package ru.tripster.domain.usecases.events

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.statistics.StatisticsUseCase
import ru.tripster.domain.model.MenuItems
import ru.tripster.domain.model.events.EventResults
import ru.tripster.domain.model.events.EventsCallSortingModel
import ru.tripster.domain.model.events.StatusStates
import ru.tripster.domain.repository.DataStoreRepository
import ru.tripster.domain.repository.events.EventsRepository

interface EventsUseCase {
    suspend fun execute(params: EventsCallSortingModel): Flow<PagingData<EventResults>>
}

class EventsUseCaseImpl(
    private val eventsRepository: EventsRepository,
    private val statistics: StatisticsUseCase,
    private val dataStoreRepository: DataStoreRepository
) : EventsUseCase {

    override suspend fun execute(params: EventsCallSortingModel): Flow<PagingData<EventResults>> {
        dataStoreRepository.saveMenuItem(MenuItems.ORDERS.type)

        var result: Flow<PagingData<EventResults>>? = null
        statistics().run {
            (this as? ActionResult.Success)?.let {
                val ordersValue = (it.result.ordersRate.value * 100).toInt()

                result = when {
                    params.isInWork == true -> {
                        eventsRepository.getEvents(params).flow.map { pagingData ->
                            pagingData.map { eventsResponse ->
                                EventResults.from(
                                    eventsResponse,
                                    ordersValue
                                )
                            }
                        }
                    }

                    params.unreadComments == 0 -> {
                        eventsRepository.getUnreadCountEvents(params).flow.map { pagingData ->
                            pagingData.map { eventsResponse ->
                                EventResults.from(
                                    eventsResponse,
                                    ordersValue
                                )
                            }
                        }
                    }

                    params.status == StatusStates.CONFIRM -> {
                        eventsRepository.getStatusDoubleSortingEvents(params).flow.map { pagingData ->
                            pagingData.map { eventsResponse ->
                                EventResults.from(
                                    eventsResponse,
                                    ordersValue
                                )
                            }
                        }
                    }

                    params.status == StatusStates.PENDING_PAYMENT -> {
                        eventsRepository.getStatusSortingEvents(params).flow.map { pagingData ->
                            pagingData.map { eventsResponse ->
                                EventResults.from(
                                    eventsResponse,
                                    ordersValue
                                )
                            }
                        }
                    }

                    params.status == StatusStates.BOOKED && params.sorting == "starts_at" -> {
                        eventsRepository.getDateEvents(params).flow.map { pagingData ->
                            pagingData.map { eventsResponse ->
                                EventResults.from(
                                    eventsResponse,
                                    ordersValue
                                )
                            }
                        }
                    }

                    params.status == StatusStates.BOOKED && params.sorting == "-starts_at" -> {
                        eventsRepository.getDateEventsLte(params).flow.map { pagingData ->
                            pagingData.map { eventsResponse ->
                                EventResults.from(
                                    eventsResponse,
                                    ordersValue
                                )
                            }
                        }
                    }

                    else -> {
                        eventsRepository.getStatusSortingEvents(params).flow.map { pagingData ->
                            pagingData.map { eventsResponse ->
                                EventResults.from(
                                    eventsResponse,
                                    ordersValue
                                )
                            }
                        }
                    }
                }
            }

            (this as? ActionResult.Error)?.let {

            }
        }
        return result ?: flow {}
    }
}