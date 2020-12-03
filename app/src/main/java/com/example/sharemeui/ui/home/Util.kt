package com.example.sharemeui.ui.home

import android.content.Context
import android.util.Log
import android.view.View
import com.example.sharemeui.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class Util(context: Context) {
    val TAG = "Util"
    val thisContext = context
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

    suspend fun insertIntoTransferQueue(title: String?, size: String?, filePath: String?, state: String?, itemView: View?): Int {
        if (transferFileDb != null) {
            val res = transferFileDb.insertAll(TransferQueueEntity(0, title, size, filePath, state))
            return 1
        } else {
            return 0
        }
    }
    suspend fun removeFromTransferQueue(data: String?, itemView: View?) {
        if (data !== null) {
            if (transferFileDb != null) {
                transferFileDb.removeFileFromQueue(data)
            }
        }
    }
    suspend fun clearFullQueue(): Int {
        if (transferFileDb != null) {
            transferFileDb.clearAll()
            return 1
        }
        return 0
    }
    suspend fun getTransferQueue(): List<TransferQueueEntity>? {
        if (transferFileDb !== null) {
            return transferFileDb.getAll()
        }
        return null
    }
    suspend fun startSendingFromQueue (servers: List<String>) {

        Log.d(TAG, "Starting Transfer Session")
        val files = this.getTransferQueue()
        if (files !== null && files.isNotEmpty()) {
            for (file in files) {
                if (file !== null) {
                    insertHistory(file.title, file.size, null, file.filePath, "0%", null, null, null, null)
                    if (servers != null && servers.isNotEmpty() && file.filePath !== null)
                        servers.forEach { CoroutineScope(IO).launch { Client(it, null).sendFile(file.filePath, thisContext) }
                    }
                }
            }
        }
    }
}