package com.example.recorpee.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PeeDao {
    @Insert
    suspend fun insert(data: PeeEntity)

    @Query("SELECT * FROM pee WHERE :from <= date AND date < :to order by date DESC")
    fun query(from: Long, to: Long): Flow<List<PeeEntity>>

    @Query("SELECT * FROM pee")
    fun queryAll(): Flow<List<PeeEntity>>

    @Update
    suspend fun update(data: PeeEntity)

    @Delete
    suspend fun  delete(data: PeeEntity)
}