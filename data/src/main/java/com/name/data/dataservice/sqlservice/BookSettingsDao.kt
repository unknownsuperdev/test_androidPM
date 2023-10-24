package com.name.data.dataservice.sqlservice

import androidx.room.*
import com.name.entities.roommodels.BookSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(item: BookSettingsEntity)

    @Query("SELECT * FROM bookSettings ORDER BY id DESC LIMIT 1 ")
    fun getItem(): BookSettingsEntity
}