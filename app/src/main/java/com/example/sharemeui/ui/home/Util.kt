package com.example.sharemeui.ui.home

import android.content.Context
import android.util.Log


class Util(context: Context) {
    val TAG = "Util"
    val historyDb = context?.let { AppDatabase.getHistoryInstance(it) }
    val transferFileDb = context?.let { AppDatabase.getTransferQueueInstance(it) }

    suspend fun insertHistory(title: String?, size: String?, coverImage: String?, filePath: String?, processCompleted: String?, type: String?, dateCreated: Long?, dateUpdated: Long?, result: String?): Int {
        if (historyDb != null) {
            val res = historyDb.insertAll(HistoryEntity(0, title, size, coverImage, filePath, processCompleted, type, dateCreated, dateUpdated, result))
            return 1
        } else {
            return 0
        }
    }

    suspend fun getAllHistory(): List<HistoryEntity>? {
        if (historyDb != null) {
            return historyDb.getAll()
        }
        return null
    }

    suspend fun clearAllHistory(): Int {
        if (historyDb != null) {
            historyDb.deleteAll()
            return 1
        }
        return 0
    }

    suspend fun insertTransferFile(title: String?, size: String?, filePath: String?, state: String?): Int {
        if (transferFileDb != null) {
            val res = transferFileDb.insertAll(TransferQueueEntity(0, title, size, filePath, state))
            return 1
        } else {
            return 0
        }
    }

    suspend fun clearFullQueue(): Int {
        if (transferFileDb != null) {
            transferFileDb.clearAll()
            return 1
        }
        return 0
    }
}