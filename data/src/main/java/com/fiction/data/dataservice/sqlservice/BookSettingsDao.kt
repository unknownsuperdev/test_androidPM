package com.fiction.data.dataservice.sqlservice

import androidx.room.*
import com.fiction.entities.roommodels.BookSettingsEntity

@Dao
interface BookSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(item: BookSettingsEntity)

    @Query("SELECT * FROM bookSettings ORDER BY id DESC LIMIT 1 ")
    fun getItem(): BookSettingsEntity
}