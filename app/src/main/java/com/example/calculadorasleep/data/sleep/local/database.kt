package com.example.calculadorasleep.data.sleep.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SleepEntity::class],
    version = 1,
    exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    abstract fun sleepDao(): SleepDao

}