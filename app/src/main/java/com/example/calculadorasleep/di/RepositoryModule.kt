package com.example.calculadorasleep.di

import com.example.calculadorasleep.data.sleep.repository.SleepRepositoryImpl
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: SleepRepositoryImpl
    ): SleepRepository


}