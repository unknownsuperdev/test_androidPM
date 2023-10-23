package ru.tripster.entities.response.orders

data class EventOrderDetailResponse(
    val date: String,
    val is_for_groups: Boolean,
    val max_persons: Int,
    val meeting_point: MeetingPointResponse,
    val number_of_persons_avail: Int,
    val aware_start_dt: String,
    val time: String,
    val email: String,
    val phone: String
)