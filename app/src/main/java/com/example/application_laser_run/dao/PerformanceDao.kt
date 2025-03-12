package com.example.application_laser_run.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.application_laser_run.model.Performance

@Dao
interface PerformanceDao {
    @Query("select * from performance")
    suspend fun getAll(): List<Performance>


    @Query("select * from performance where id = :id")
    suspend fun getById(id: Long): Performance?

    @Query("select * from performance order by id desc limit 1")
    suspend fun getLastPerformance(): Performance

    @Query("select avg(total_duration) from performance where category_id = :category_id")
    suspend fun getAvgTotalDuration(category_id: Int): Long?

    @Query("select avg(run_duration) from performance where category_id = :category_id")
    suspend fun getAvgRunDuration(category_id: Int): Long?

    @Query("select avg(shoot_duration) from performance where category_id = :category_id")
    suspend fun getAvgShootDuration(category_id: Int): Long?

    @Query("select avg(missed_targets) from performance where category_id = :category_id")
    suspend fun getAvgTargetsMissed(category_id: Int): Long?


    @Insert
    suspend fun insert(vararg performance: Performance)

    @Delete
    suspend fun delete(performance: Performance)


}