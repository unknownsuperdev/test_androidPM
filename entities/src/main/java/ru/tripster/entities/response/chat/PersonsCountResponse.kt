package ru.tripster.entities.response.chat

data class PersonsCountResponse(
    val new_value: Int?,
    val old_value: Int?
) {
    companion object {
        fun empty(): PersonsCountResponse =
            PersonsCountResponse(
                new_value = 0,
                old_value = 0
            )
    }
}