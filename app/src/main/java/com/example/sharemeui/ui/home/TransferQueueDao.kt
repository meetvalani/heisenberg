package com.example.sharemeui.ui.home

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransferQueueDao {
    @Query("SELECT * FROM TransferQueueEntity")
    suspend fun getAll(): List<TransferQueueEntity>

    @Insert
    suspend fun insertAll(vararg task: TransferQueueEntity)

    @Delete
    suspend fun clear(task: TransferQueueEntity)

    @Query("DELETE FROM TransferQueueEntity")
    suspend fun clearAll()
}
