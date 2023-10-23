package ru.tripster.entities.response.calendar.dateOrder

import com.google.gson.annotations.SerializedName

data class EventDataResponse(
    @SerializedName("aware_start_dt")
    val aware_start_dt: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("experience")
    val experience: ExperienceResponseModel?,
    @SerializedName("guide_last_visit_date")
    val guide_last_visit_date: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_for_groups")
    val is_for_groups: Boolean?,
    @SerializedName("max_persons")
    val max_persons: Int?,
    @SerializedName("number_of_persons_avail")
    val number_of_persons_avail: Int?,
    @SerializedName("number_of_persons_overall")
    val number_of_persons_overall: Int?,
    @SerializedName("number_of_persons_paid")
    val number_of_persons_paid: Int?,
    @SerializedName("orders")
    val orders: List<OrderResponse>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("time")
    val time: String?
)