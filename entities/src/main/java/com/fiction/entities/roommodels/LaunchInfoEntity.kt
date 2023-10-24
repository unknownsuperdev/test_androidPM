package com.fiction.entities.roommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fiction.entities.utils.Constants.Companion.LAUNCH_INFO_STATUS_ID


@Entity(tableName = "launchInfoEntity")
data class LaunchInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = LAUNCH_INFO_STATUS_ID,
    @ColumnInfo(name = "is_install")
    var isInstall: Boolean = true
)

