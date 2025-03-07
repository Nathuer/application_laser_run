package com.example.application_laser_run.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Performance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "total_duration") val totalDuration: Long,
    @ColumnInfo(name = "run_duration") val runDuration: Long,
    @ColumnInfo(name = "avg_speed") val avgSpeed: Int,
    @ColumnInfo(name = "shoot_duration") val shootDuration: Long,
    @ColumnInfo(name = "shoot_min_duration") val shootMinDuration: Long,
    @ColumnInfo(name = "shoot_avg_duration") val shootAvgDuration: Long,
    @ColumnInfo(name = "shoot_max_duration") val shootMaxDuration: Long,
    @ColumnInfo(name = "missed_targets") val missedTargets: Int,
    @ColumnInfo(name = "category_id") val categoryId: Int
)