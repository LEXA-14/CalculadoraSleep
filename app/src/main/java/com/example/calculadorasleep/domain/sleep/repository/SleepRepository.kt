package com.example.calculadorasleep.domain.sleep.repository

import com.example.calculadorasleep.domain.sleep.model.Sleep
import kotlinx.coroutines.flow.Flow

interface SleepRepository{

    fun getAll(): Flow<List<Sleep>>
    suspend fun getSince(since: Long):List<Sleep>
    suspend fun getById(id: Int)
    suspend fun update(sleep: Sleep)
    suspend fun delete(sleep: Sleep)
}