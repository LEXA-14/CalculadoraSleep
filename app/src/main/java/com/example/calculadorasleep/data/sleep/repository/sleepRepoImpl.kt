package com.example.calculadorasleep.data.sleep.repository

import coil.decode.DataSource
import com.example.calculadorasleep.data.sleep.local.SleepDao
import com.example.calculadorasleep.data.sleep.mapper.toDomain
import com.example.calculadorasleep.data.sleep.mapper.toEntity
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SleepRepositoryImpl @Inject constructor(
    private val localDataSource: SleepDao
) : SleepRepository {

    override fun getAll(): Flow<List<Sleep>> {
        return localDataSource.getAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getSince(since: Long): Flow<List<Sleep>> {
        return localDataSource.getSince(since).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: Int): Sleep? {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(sleep: Sleep){
        localDataSource.upsert(sleep.toEntity())
    }

    override suspend fun delete(sleep: Sleep) {
        localDataSource.delete(sleep.toEntity())
    }
}