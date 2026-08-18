package com.example.calculadorasleep.data.sleep.local.sleep

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep")
data class SleepEntity (
    @PrimaryKey(autoGenerate = true)
    val sleepId:Int=0,
    val userId: String,
    val dormirTiempo: Long,
    val despertarTiempo: Long,
    val ciclos: Int,
    val calidadSleep:Int?=null

)