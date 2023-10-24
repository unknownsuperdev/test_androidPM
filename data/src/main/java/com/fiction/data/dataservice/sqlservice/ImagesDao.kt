package com.fiction.data.dataservice.sqlservice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fiction.entities.roommodels.ImagesEntity

@Dao
interface ImagesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertImage(item: ImagesEntity)

    @Query("SELECT * FROM images WHERE `key` = :key")
    fun getImage(key: String): ImagesEntity?
}