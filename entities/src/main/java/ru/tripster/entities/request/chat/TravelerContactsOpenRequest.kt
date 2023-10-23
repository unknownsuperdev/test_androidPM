package ru.tripster.entities.request.chat

import com.google.gson.annotations.SerializedName

data class TravelerContactsOpenRequest(
    @SerializedName("traveler_contacts_open")
    val traveler_contacts_open: Boolean?,
)
