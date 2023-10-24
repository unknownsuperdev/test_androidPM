package com.name.data.dataservice.sqlservice

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.name.entities.roommodels.BookSettingsEntity

@Database(
    entities = [BookSettingsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): BookSettingsDao
}