package com.example.sharemeui.ui.home

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransferQueueDao {
    @Query("SELECT * FROM TransferQueueEntity")
    fun getAll(): List<TransferQueueEntity>

    @Insert
    fun insertAll(vararg task: TransferQueueEntity)

    @Delete
    fun clear(task: TransferQueueEntity)

    @Query("DELETE FROM TransferQueueEntity")
    fun clearAll()
}
