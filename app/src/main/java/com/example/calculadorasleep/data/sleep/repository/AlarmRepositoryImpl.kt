package com.example.calculadorasleep.data.sleep.repository

import com.example.calculadorasleep.data.sleep.local.alarm.AlarmDao
import com.example.calculadorasleep.data.sleep.mapper.toDomain
import com.example.calculadorasleep.data.sleep.mapper.toEntity
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val localDataSource: AlarmDao,
    private val auth: FirebaseAuth
) : AlarmRepository {

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    private val authStateFlow: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun observeAlarms(): Flow<List<Alarm>> {
         return authStateFlow.flatMapLatest { uid ->
            if (uid == null) {
                flowOf(emptyList())
            } else {
                localDataSource.observeAlarms(uid).map { entities ->
                    entities.map { it.toDomain() }
                }
            }
        }
    }

    override suspend fun getAlarm(id: Int): Alarm? =
         localDataSource.getAlarmById(id)?.toDomain()

    override suspend fun upsert(alarm: Alarm): Int {
        val uid=currentUserId ?: return -1
        localDataSource.upsertAlarm(alarm.toEntity(uid))
        return alarm.alarmId
    }

    override suspend fun delete(id: Int) =
        localDataSource.deleteAlarmById(id)
}