package com.example.sharemeui.ui.home

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(HistoryEntity::class, TransferQueueEntity::class), version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun TransferQueueDao(): TransferQueueDao
    companion object {
        private const val DB_NAME = "MyAppDb.db"
        private var INSTANCE : AppDatabase? = null

        fun getHistoryInstance(context: Context): HistoryDao? {
            if (INSTANCE === null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DB_NAME
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE?.historyDao()
        }

        fun getTransferQueueInstance(context: Context): TransferQueueDao? {
            if (INSTANCE === null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DB_NAME
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE?.TransferQueueDao()
        }
    }
}