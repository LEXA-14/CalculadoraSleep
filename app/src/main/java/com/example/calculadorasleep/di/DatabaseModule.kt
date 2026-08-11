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
        ).build()
    }

    @Provides
    @Singleton
    fun provideSleepDao(database: SleepDatabase): SleepDao {
        return database.sleepDao()
    }
}