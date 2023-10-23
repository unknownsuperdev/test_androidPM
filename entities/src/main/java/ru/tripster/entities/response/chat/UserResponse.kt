package ru.tripster.entities.response.chat

data class UserResponse(
    val avatar150_url: String?,
    val avatar30_url: String?,
    val first_name: String?,
    val id: Int?
) {
    companion object {
        fun empty(): UserResponse =
            UserResponse(
                avatar150_url = "",
                avatar30_url = "",
                first_name = "",
                id = 0
            )
    }
}