package com.example.application_laser_run.activity

import android.app.Application
import android.content.Context
import android.os.SystemClock
import com.example.application_laser_run.database.AppDatabase
import com.example.application_laser_run.dao.PerformanceDao
import kotlin.concurrent.thread

class MyApplication : Application() {

    private var db: AppDatabase? = null

    val start = SystemClock.elapsedRealtime()
    var roundCount: Int = 0
    var chronometer: Long = 0
    var chronometerForRuntime: Long = 0
    var missedTargets: Int = 0

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this)

        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val hasClearedDb = sharedPreferences.getBoolean("hasClearedDb", false)

        if (!hasClearedDb) {
            thread {
                db?.clearAllTables()
                sharedPreferences.edit().putBoolean("hasClearedDb", true).apply()
            }
        }
    }

    // Fournir l'accès au DAO
    fun performanceDao(): PerformanceDao {
        return db?.performanceDao() ?: throw IllegalStateException("Database not initialized")
    }

    companion object {
        // Singleton pattern pour garantir une seule instance de MyApplication
        private var instance: MyApplication? = null

        fun getInstance(application: Context): MyApplication {
            if (instance == null) {
                instance = application as MyApplication
            }
            return instance!!
        }
    }
}
