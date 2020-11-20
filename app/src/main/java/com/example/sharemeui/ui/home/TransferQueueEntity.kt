package com.example.sharemeui.ui.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// possible stats are (INQUEUE)

@Entity
data class TransferQueueEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "size") val size: String?,
    @ColumnInfo(name = "filePath") val filePath: String?,
    @ColumnInfo(name = "state") val state: String?
)

