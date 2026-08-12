package com.example.calculadorasleep.domain.sleep.repository

import com.example.calculadorasleep.domain.sleep.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun observeAlarms(): Flow<List<Alarm>>
    suspend fun getAlarm(id: Int): Alarm?
    suspend fun upsert(alarm: Alarm): Int
    suspend fun delete(id: Int)
}