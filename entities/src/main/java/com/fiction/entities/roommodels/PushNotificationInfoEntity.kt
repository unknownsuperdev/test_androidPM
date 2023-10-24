package com.fiction.entities.roommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pushNotifications")
data class PushNotificationInfoEntity(
    @PrimaryKey()
    @ColumnInfo(name = "not_id")
    val notId: Int?
)
