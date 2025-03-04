package com.example.application_laser_run.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.application_laser_run.dao.PerformanceDao
import com.example.application_laser_run.model.Performance

@Database(entities = [Performance::class], version = 2) // Version 2
abstract class AppDatabase : RoomDatabase() {
    abstract fun performanceDao(): PerformanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "laser_run_db"
                )
                    .addMigrations(MIGRATION_1_2) // Ajoute la migration ici
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Migration pour ajouter le champ start_time
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Ajoute une colonne start_time de type INTEGER
                database.execSQL("ALTER TABLE performance ADD COLUMN start_time INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
