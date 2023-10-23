package ru.tripster.entities.response.chat

data class DateTimeResponse(
    val new_value: String?,
    val old_value: String?
) {
    companion object {
        fun empty(): DateTimeResponse =
            DateTimeResponse(
                new_value = "",
                old_value = ""
            )
    }
}