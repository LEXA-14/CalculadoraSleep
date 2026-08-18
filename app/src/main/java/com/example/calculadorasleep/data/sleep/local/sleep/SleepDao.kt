package com.example.calculadorasleep.data.sleep.local.sleep

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.calculadorasleep.data.sleep.local.sleep.SleepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {

    @Upsert
    suspend fun upsert(sleep: SleepEntity)

    @Delete
    suspend fun delete(sleep: SleepEntity)
    @Query("Delete from sleep where sleepId= :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM sleep where userId = :userId ORDER BY dormirTiempo DESC")
    fun getAll(userId: String): Flow<List<SleepEntity>>

    @Query("SELECT * FROM sleep WHERE  userId and dormirTiempo >= :sinceMillis ORDER BY dormirTiempo DESC")
    fun getSince(userId: String,sinceMillis: Long): Flow<List<SleepEntity>>

    @Query("SELECT * FROM sleep WHERE sleepId = :id")
    suspend fun getById(id: Int): SleepEntity?
}