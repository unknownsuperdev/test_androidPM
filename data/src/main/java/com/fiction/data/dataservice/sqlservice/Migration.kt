package com.fiction.data.dataservice.sqlservice

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {
    val MIGRATION_2_3 = object : Migration(2, 3){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `pushNotifications` (`not_id` INTEGER, PRIMARY KEY(`not_id`))")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create a new table with the desired schema
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `launchInfoEntity` ( `id` INTEGER PRIMARY KEY NOT NULL, `is_install` INTEGER NOT NULL)"
            )
        }
    }

}