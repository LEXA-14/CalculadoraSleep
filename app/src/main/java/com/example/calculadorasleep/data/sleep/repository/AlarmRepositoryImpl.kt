package com.example.calculadorasleep.data.sleep.repository

import com.example.calculadorasleep.data.sleep.local.alarm.AlarmDao
import com.example.calculadorasleep.data.sleep.mapper.toDomain
import com.example.calculadorasleep.data.sleep.mapper.toEntity
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val localDataSource: AlarmDao
) : AlarmRepository {

    override fun observeAlarms(): Flow<List<Alarm>> =
        localDataSource.observeAlarms().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getAlarm(id: Int): Alarm? =
        localDataSource.getAlarmById(id)?.toDomain()

    override suspend fun upsert(alarm: Alarm): Int {
        localDataSource.upsertAlarm(alarm.toEntity())
        return alarm.alarmId
    }

    override suspend fun delete(id: Int) =
        localDataSource.deleteAlarmById(id)
}