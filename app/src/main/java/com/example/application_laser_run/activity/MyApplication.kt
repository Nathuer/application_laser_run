package com.example.application_laser_run.activity

import android.app.Application
import android.os.SystemClock

class MyApplication : Application() {
    val start = SystemClock.elapsedRealtime()
    var roundCount: Int = 0

}