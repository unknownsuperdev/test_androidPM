package ru.tripster.entities.response.chat

data class FullPriceResponse(
    val new_value: Double?,
    val old_value: Double?
) {
    companion object {
        fun empty(): FullPriceResponse =
            FullPriceResponse(
                new_value = 0.0,
                old_value = 0.0
            )
    }
}