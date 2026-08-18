package com.example.calculadorasleep.data.sleep.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculadorasleep.data.sleep.local.alarm.AlarmDao
import com.example.calculadorasleep.data.sleep.local.alarm.AlarmEntity
import com.example.calculadorasleep.data.sleep.local.sleep.SleepDao
import com.example.calculadorasleep.data.sleep.local.sleep.SleepEntity

@Database(entities = [
    SleepEntity::class,
    AlarmEntity::class
    ],
    version = 3,
    exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    abstract fun sleepDao(): SleepDao

    abstract fun alarmDao() : AlarmDao

}