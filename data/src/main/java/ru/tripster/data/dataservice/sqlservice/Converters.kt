package ru.tripster.data.dataservice.sqlservice

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.tripster.entities.response.chat.SystemEventDataResponse
import ru.tripster.entities.response.chat.UserResponse

class Converters {

    @TypeConverter
    fun toSystemEventDataResponse(listOfVisits: SystemEventDataResponse): String {
        return Gson().toJson(listOfVisits).toString()
    }

    @TypeConverter
    fun fromSystemEventDataResponse(value: String): SystemEventDataResponse {
        val listOfVisits = object : TypeToken<SystemEventDataResponse>() {}.type
        return Gson().fromJson(value, listOfVisits)
    }

//    @TypeConverter
//    fun toChangesResponse(listOfVisits: ChangesResponse): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromChangesResponse(value: String): ChangesResponse {
//        val listOfVisits = object : TypeToken<ChangesResponse>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }
//
//    @TypeConverter
//    fun toConfirmedResponse(listOfVisits: ConfirmedResponse): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromConfirmedResponse(value: String): ConfirmedResponse {
//        val listOfVisits = object : TypeToken<ConfirmedResponse>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }
//
//    @TypeConverter
//    fun toDateTimeResponse(listOfVisits: DateTimeResponse): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromDateTimeResponse(value: String): DateTimeResponse {
//        val listOfVisits = object : TypeToken<DateTimeResponse>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }
//
//    @TypeConverter
//    fun toFullPriceResponse(listOfVisits: FullPriceResponse): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromFullPriceResponse(value: String): FullPriceResponse {
//        val listOfVisits = object : TypeToken<FullPriceResponse>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }
//
//    @TypeConverter
//    fun toPersonsCountResponse(listOfVisits: PersonsCountResponse): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromPersonsCountResponse(value: String): PersonsCountResponse {
//        val listOfVisits = object : TypeToken<PersonsCountResponse>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }
//
//    @TypeConverter
//    fun toStatusResponse(listOfVisits: StatusResponse): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromStatusResponse(value: String): StatusResponse {
//        val listOfVisits = object : TypeToken<StatusResponse>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }
//
//    @TypeConverter
//    fun toTicketsResponse(listOfVisits: TicketsResponse): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromTicketsResponse(value: String): TicketsResponse {
//        val listOfVisits = object : TypeToken<TicketsResponse>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }
//
//    @TypeConverter
//    fun toListAdditionalPropResponse(listOfVisits: List<AdditionalPropResponse>): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromListAdditionalPropResponse(value: String): List<AdditionalPropResponse> {
//        val listOfVisits = object : TypeToken<List<AdditionalPropResponse>>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }
//
//
//    @TypeConverter
//    fun toListTicketResponse(listOfVisits: List<TicketResponse>?): String {
//        return Gson().toJson(listOfVisits).toString()
//    }
//
//    @TypeConverter
//    fun fromListTicketResponse(value: String): List<TicketResponse>? {
//        val listOfVisits = object : TypeToken<List<TicketResponse>?>() {}.type
//        return Gson().fromJson(value, listOfVisits)
//    }

    @TypeConverter
    fun toUserDataResponse(listOfVisits: UserResponse): String {
        return Gson().toJson(listOfVisits).toString()
    }

    @TypeConverter
    fun fromUserDataResponse(value: String): UserResponse {
        val listOfVisits = object : TypeToken<UserResponse>() {}.type
        return Gson().fromJson(value, listOfVisits)
    }
}

//    @TypeConverter
//    fun toSystemEventDataResponse(value: String) = enumValueOf<SystemEventDataResponse>(value)
//
//    @TypeConverter
//    fun fromLineHeight(value: SystemEventDataResponse) = value.name

