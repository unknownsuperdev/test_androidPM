package ru.tripster.entities.response.chat

data class TicketsResponse(
    val new_value: List<AdditionalPropResponse>?,
    val old_value: List<AdditionalPropResponse>?
) {
    companion object {
        fun empty(): TicketsResponse =
            TicketsResponse(
                new_value = emptyList(),
                old_value = emptyList()
            )
    }
}