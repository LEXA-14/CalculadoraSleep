package com.example.calculadorasleep.domain.sleep.repository

import com.example.calculadorasleep.domain.sleep.model.Sleep
import kotlinx.coroutines.flow.Flow

interface SleepRepository{

    fun getAll(): Flow<List<Sleep>>
    suspend fun getSince(since: Long):Flow<List<Sleep>>
    suspend fun getById(id: Int): Sleep?
    suspend fun upsert(sleep: Sleep)
    suspend fun delete(sleep: Sleep)
}