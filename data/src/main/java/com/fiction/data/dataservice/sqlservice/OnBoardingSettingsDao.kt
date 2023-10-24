package com.fiction.data.dataservice.sqlservice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fiction.entities.roommodels.OnBoardingSettingsEntity

@Dao
interface OnBoardingSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(item: OnBoardingSettingsEntity)

    @Query("SELECT * FROM onBoardingSettings")
    fun getOnBoardingSettings(): OnBoardingSettingsEntity?

    @Query("DELETE FROM onBoardingSettings")
    fun deleteTable()
}
