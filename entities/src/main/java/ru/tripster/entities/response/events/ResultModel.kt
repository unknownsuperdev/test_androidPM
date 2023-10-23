package ru.tripster.entities.response.events

import com.google.gson.annotations.SerializedName

data class ResultModel(
    @SerializedName("experience")
    val experience: ExperienceModel?,
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
    val orders: List<OrderModel>?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("guide_last_visit_date")
    val guide_last_visit_date: String?,
    @SerializedName("last_modified_date")
    val last_modified_date: String?,
    @SerializedName("aware_start_dt")
    val aware_start_dt: String?
)