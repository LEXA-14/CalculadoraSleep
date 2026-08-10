package com.example.calculadorasleep.domain.sleep.model

data class Sleep (
    val sleepId:Int=0,
    val dormirTiempo: Long,
    val despertarTiempo: Long,
    val ciclos: Int,
    val calidadSleep:Int?=null
)
