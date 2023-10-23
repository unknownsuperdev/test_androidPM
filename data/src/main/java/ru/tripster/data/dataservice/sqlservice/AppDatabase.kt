package ru.tripster.data.dataservice.sqlservice

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.tripster.data.dataservice.sqlservice.chat.OrderCommentsDao
import ru.tripster.entities.room.chat.OrderCommentsData

@Database(
    entities = [OrderCommentsData::class],
    version = 16,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderCommentsDao(): OrderCommentsDao
}