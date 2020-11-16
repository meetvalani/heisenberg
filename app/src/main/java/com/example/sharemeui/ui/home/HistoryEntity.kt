package com.example.sharemeui.ui.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "size") val size: String?,
    @ColumnInfo(name = "coverImage") val coverImage: String?,
    @ColumnInfo(name = "processCompleted") val processCompleted: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "dateCreated") val dateCreated: Long?,
    @ColumnInfo(name = "dateUpdated") val dateUpdated: Long?,
    @ColumnInfo(name = "result") val result: String?
)

