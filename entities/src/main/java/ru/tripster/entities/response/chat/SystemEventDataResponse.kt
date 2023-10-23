package ru.tripster.entities.response.chat


data class SystemEventDataResponse(
    val changes: ChangesResponse?,
    val number_of_persons: Int?,
    val reason: String?,
    val tickets: List<TicketResponse>?
) {
    companion object{
        fun empty(): SystemEventDataResponse =
            SystemEventDataResponse(
                changes = ChangesResponse.empty(),
                number_of_persons = 0,
                reason = "",
                tickets = emptyList()
            )
    }

}