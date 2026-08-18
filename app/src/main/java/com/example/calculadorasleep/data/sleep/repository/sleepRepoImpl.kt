package com.example.calculadorasleep.data.sleep.repository

import com.example.calculadorasleep.data.sleep.local.sleep.SleepDao
import com.example.calculadorasleep.data.sleep.mapper.toDomain
import com.example.calculadorasleep.data.sleep.mapper.toEntity
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SleepRepositoryImpl @Inject constructor(
    private val localDataSource: SleepDao,
    private val auth: FirebaseAuth
) : SleepRepository {

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    override fun getAll(): Flow<List<Sleep>> {
        val uid = currentUserId ?: return flowOf(emptyList())
        return localDataSource.getAll(uid).map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getSince(since: Long): Flow<List<Sleep>> {
        val uid = currentUserId ?: return flowOf(emptyList())
        return localDataSource.getSince(uid,since).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: Int): Sleep? {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(sleep: Sleep){
        val uid=currentUserId ?: return
        localDataSource.upsert(sleep.toEntity(uid))
    }

    override suspend fun delete(sleep: Sleep) {
        localDataSource.deleteById(sleep.sleepId)
    }
}