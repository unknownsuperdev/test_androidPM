package ru.tripster.data.dataservice.sqlservice.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.tripster.entities.room.chat.OrderCommentsData
import ru.tripster.entities.utils.Constants

@Dao
interface OrderCommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(item: OrderCommentsData):Long

    @Query("SELECT * FROM ${Constants.CHAT_TABLE_NAME} WHERE orderId IN  (:orderId) ")
    fun getCommentsFromDb(orderId: Int): Flow<List<OrderCommentsData>>

    @Query("SELECT * FROM ${Constants.CHAT_TABLE_NAME} WHERE `key` IN  (:uuid) ")
    fun getItemByKey(uuid: String): OrderCommentsData

    @Query("SELECT * FROM ${Constants.CHAT_TABLE_NAME} WHERE `messageId` IN  (:messageId) ")
    fun getItemByMessageId(messageId: Int): OrderCommentsData

    @Query("SELECT EXISTS(SELECT * FROM ${Constants.CHAT_TABLE_NAME} WHERE messageId = :id)")
    fun getItemById(id: Int): Boolean
}


