package ru.tripster.entities.response.chat

data class ResultResponse(
    val comment: String?,
    val id: Int?,
    val include_contacts: Boolean?,
    val submit_date: String?,
    val system_event_data: SystemEventDataResponse?,
    val system_event_type: String?,
    val user: UserResponse?,
    val viewed_by_guide: Boolean?,
    val viewed_by_traveler: Boolean?,
    val user_role: String?
)