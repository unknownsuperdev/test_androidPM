package ru.tripster.entities.response.chat

data class ConfirmedResponse(
    val new_value: Boolean?,
    val old_value: Boolean?
) {
    companion object {
        fun empty(): ConfirmedResponse =
            ConfirmedResponse(
                new_value = false,
                old_value = false
            )
    }
}