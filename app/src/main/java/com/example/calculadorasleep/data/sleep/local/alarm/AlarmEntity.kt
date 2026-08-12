package com.example.calculadorasleep.data.sleep.local.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity (
    @PrimaryKey(autoGenerate = true)
    val alarmId: Int = 0,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean,
    val label: String
)