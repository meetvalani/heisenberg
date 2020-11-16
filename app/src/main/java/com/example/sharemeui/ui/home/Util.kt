package com.example.sharemeui.ui.home

import android.content.Context
import android.util.Log


class Util(context: Context) {
    val TAG = "Util"
    val historyDb = context?.let { AppDatabase.getInstance(it) }

    fun insertHistory(title: String?, size: String?, coverImage: String?, processCompleted: String?, type: String?, dateCreated: Long?, dateUpdated: Long?, result: String?): Int {
        if (historyDb != null) {
            val res = historyDb.insertAll(HistoryEntity(0, title, size, coverImage, processCompleted, type, dateCreated, dateUpdated, result))
            return 1
        } else {
            return 0
        }
        return 0
    }

    fun getAllHistory(): List<HistoryEntity>? {
        if (historyDb != null) {
            return historyDb.getAll()
        }
        return null
    }

    fun clearAllHistory(): Int {
        if (historyDb != null) {
            historyDb.deleteAll()
            return 1
        }
        return 0
    }
}