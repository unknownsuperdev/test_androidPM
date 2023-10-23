package ru.tripster.entities.room.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.tripster.entities.response.chat.SystemEventDataResponse
import ru.tripster.entities.response.chat.UserResponse
import ru.tripster.entities.utils.Constants.Companion.CHAT_TABLE_NAME

@Entity(tableName = CHAT_TABLE_NAME)
data class OrderCommentsData(
    @ColumnInfo(name = "comment")
    var comment: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val autogenId: Int = 0,
    @ColumnInfo(name = "messageId")
    val messageId: Int,
    @ColumnInfo(name = "includeContacts")
    val includeContacts: Boolean,
    @ColumnInfo(name = "submitDate")
    val submitDate: String,
    @ColumnInfo(name = "systemEventData")
    val systemEventData: SystemEventDataResponse,
    @ColumnInfo(name = "systemEventType")
    val systemEventType: String,
    @ColumnInfo(name = "user")
    val user: UserResponse,
    @ColumnInfo(name = "orderId")
    val orderId: Int,
    @ColumnInfo(name = "viewedByGuide")
    var viewedByGuide: Boolean,
    @ColumnInfo(name = "viewedByTraveler")
    var viewedByTraveler: Boolean,
    @ColumnInfo(name = "userRole")
    val userRole: String,
    @ColumnInfo(name = "key")
    val key: String,
    @ColumnInfo(name = "error")
    var error: String = "",
    @ColumnInfo(name = "currency")
    var currency: String = "",
    @ColumnInfo(name = "nextPage")
    var nextPage: String
)
