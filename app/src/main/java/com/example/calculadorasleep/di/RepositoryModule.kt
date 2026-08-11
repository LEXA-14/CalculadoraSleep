package com.example.calculadorasleep.di

import com.example.calculadorasleep.data.sleep.repository.SleepRepositoryImpl
import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSleepRepository(
        impl: SleepRepositoryImpl
    ): SleepRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }
    }
}