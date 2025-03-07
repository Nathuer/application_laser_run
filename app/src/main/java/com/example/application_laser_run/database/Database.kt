package com.example.application_laser_run.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.application_laser_run.dao.PerformanceDao
import com.example.application_laser_run.model.Performance

@Database(entities = [Performance::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun performanceDao(): PerformanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "laser_run_data"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
