package com.example.application_laser_run.model

import android.app.Application

class MyApplication : Application() {
    var nbTour: Int = 0
    var totalDuration: Long = 0
    var shootMinDuration: Long = 50000000000
    var shootMaxDuration: Long = 0
    var avgShootDuration: Long = 0
    var shootDuration = mutableListOf<Long>()
    var currentTour: Int = 0
    var runDuration: Long = 0
    var avgSpeed: Int = 0
    var shotDuration: Long = 0
    var missedTargets: Int = 0
    var categorie: Int = 0

    var lapCountInCategory: Int = 0
    var initialDistanceInCategory: Int = 0
    var lapDistanceInCategory: Int = 0



}