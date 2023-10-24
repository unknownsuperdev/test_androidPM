package com.fiction.data.dataservice.sqlservice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fiction.entities.roommodels.PushNotificationInfoEntity

@Dao
interface PushNotificationInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(item: PushNotificationInfoEntity)

    @Query("SELECT COUNT(*) FROM pushNotifications")
    fun getNotificationSize(): Int?

    @Query("DELETE FROM pushNotifications WHERE not_id = :notId")
    fun deleteNotId(notId: Int)

    @Query("SELECT not_id FROM pushNotifications ORDER BY not_id DESC LIMIT 1")
    fun getLastAddedNoteId(): Int?

    @Query("SELECT not_id FROM pushNotifications ORDER BY not_id ASC LIMIT 1")
    fun getFirstAddedNoteId(): Int?
}