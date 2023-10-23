package ru.tripster.entities.responce.response.events

import com.google.gson.annotations.SerializedName

data class NumberOfPersonsAvailRequest(
    @SerializedName("number_of_persons_avail")
    val numberOfPersonsAvail : Int?,
    @SerializedName("guide_last_visit_date")
    val guideLastVisitDate : String
)
