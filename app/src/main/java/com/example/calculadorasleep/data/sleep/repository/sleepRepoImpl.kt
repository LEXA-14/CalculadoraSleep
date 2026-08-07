package com.example.calculadorasleep.data.sleep.repository

import com.example.calculadorasleep.data.sleep.local.SleepDao
import com.example.calculadorasleep.data.sleep.mapper.toDomain
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SleepRepositoryImpl @Inject constructor(
    private val dao: SleepDao
) : SleepRepository {

    override fun getAll(): Flow<List<Sleep>> {
        return dao.getAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getSince(since: Long): List<Sleep> {
        return dao.getSince(since).map { it.toDomain() }
    }

    override suspend fun getById(id: Int): Sleep? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun update(sleep: Sleep) {
        dao.upsert(sleep.toEntity())
    }

    override suspend fun delete(sleep: Sleep) {
        dao.delete(sleep.toEntity())
    }
}