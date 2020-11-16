package com.example.sharemeui.ui.home

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM historyentity")
    fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM historyentity order by dateCreated desc")
    fun getAllByDateDesc(): List<HistoryEntity>

    @Query("SELECT * FROM historyentity WHERE uid IN (:historyIds)")
    fun loadAllByIds(historyIds: IntArray): List<HistoryEntity>

    @Query("Delete from historyentity")
    fun deleteAll()

    @Insert
    fun insertAll(vararg history: HistoryEntity)

    @Delete
    fun delete(history: HistoryEntity)
}
