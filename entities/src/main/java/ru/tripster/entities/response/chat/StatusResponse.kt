package ru.tripster.entities.response.chat

data class StatusResponse(
    val new_value: String?,
    val old_value: String?
) {
    companion object {
        fun empty(): StatusResponse =
            StatusResponse(
                new_value = "",
                old_value = ""
            )
    }
}