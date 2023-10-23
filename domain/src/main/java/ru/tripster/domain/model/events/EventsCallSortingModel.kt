package ru.tripster.domain.model.events

data class EventsCallSortingModel(
    val status: StatusStates,
    val startDate: String = "",
    val sorting: String = "",
    val isInWork: Boolean? = null,
    val unreadComments: Int? = null,
    val pageSize: Int = 10
)