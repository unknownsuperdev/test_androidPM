package com.fiction.data.dataservice.sqlservice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fiction.entities.roommodels.LaunchInfoEntity

@Dao
interface LaunchInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(item: LaunchInfoEntity)

    @Query("SELECT is_install FROM launchInfoEntity WHERE id = :id")
    fun getIsInstall(id: Int): Boolean?

}