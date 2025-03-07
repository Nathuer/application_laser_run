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

    @Insert
    suspend fun insert(vararg performance: Performance)

    @Delete
    suspend fun delete(performance: Performance)


}