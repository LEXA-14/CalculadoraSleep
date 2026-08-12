package com.example.calculadorasleep.di

import android.content.Context
import androidx.room.Room
import com.example.calculadorasleep.data.sleep.local.SleepDatabase
import com.example.calculadorasleep.data.sleep.local.alarm.AlarmDao
import com.example.calculadorasleep.data.sleep.local.sleep.SleepDao
import com.example.calculadorasleep.presentation.alarm.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSleepDatabase(
        @ApplicationContext context: Context
    ): SleepDatabase {
        return Room.databaseBuilder(
            context,
            SleepDatabase::class.java,
            "sleep_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSleepDao(database: SleepDatabase): SleepDao {
        return database.sleepDao()
    }

    @Provides
    @Singleton
    fun provideAlarmDao(database: SleepDatabase): AlarmDao {
        return database.alarmDao()
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context
    ): AlarmScheduler {
        return AlarmScheduler(context)
    }
}