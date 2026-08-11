package com.example.calculadorasleep.di

import android.content.Context
import androidx.room.Room
import com.example.calculadorasleep.data.sleep.local.SleepDao
import com.example.calculadorasleep.data.sleep.local.SleepDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSleepDatabase(
        @ApplicationContext context: Context
    ): SleepDatabase {
        return Room.databaseBuilder(
            context,
            SleepDatabase::class.java,
            "ocupacion_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideOcupacionDao(database: SleepDatabase): SleepDao {
        return database.sleepDao()
    }
}