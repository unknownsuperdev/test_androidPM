package com.fiction.data.dataservice.sqlservice

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fiction.entities.roommodels.*

@Database(
    entities = [BookSettingsEntity::class, OnBoardingSettingsEntity::class, ImagesEntity::class, BooksEntity::class, ChapterEntity::class, PushNotificationInfoEntity::class, LaunchInfoEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): BookSettingsDao
    abstract fun onBoardingSettingDao(): OnBoardingSettingsDao
    abstract fun imagesDao(): ImagesDao
    abstract fun bookChapterDao(): BookChapterDao
    abstract fun pushNotificationDao(): PushNotificationInfoDao
    abstract fun launchInfoDao(): LaunchInfoDao
}