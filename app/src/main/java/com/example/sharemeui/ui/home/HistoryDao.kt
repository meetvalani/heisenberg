package com.example.sharemeui.ui.home

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM historyentity")
    suspend fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM historyentity order by dateCreated desc")
    suspend fun getAllByDateDesc(): List<HistoryEntity>

    @Query("SELECT * FROM historyentity WHERE uid IN (:historyIds)")
    suspend fun loadAllByIds(historyIds: IntArray): List<HistoryEntity>

    @Query("Delete from historyentity")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg history: HistoryEntity)

    @Delete
    suspend fun delete(history: HistoryEntity)
}
