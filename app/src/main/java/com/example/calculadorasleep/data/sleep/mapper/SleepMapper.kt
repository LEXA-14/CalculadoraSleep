package com.example.calculadorasleep.data.sleep.mapper

import com.example.calculadorasleep.data.sleep.local.SleepEntity
import com.example.calculadorasleep.domain.sleep.model.Sleep

fun SleepEntity.toDomain(): Sleep = Sleep(
    sleepId = sleepId,
    dormirTiempo = dormirTiempo,
    despertarTiempo = despertarTiempo,
    ciclos = ciclos,
    calidadSleep = calidadSleep
)

fun Sleep.toEntity(): SleepEntity = SleepEntity(
    sleepId = sleepId,
    dormirTiempo = dormirTiempo,
    despertarTiempo = despertarTiempo,
    ciclos = ciclos,
    calidadSleep = calidadSleep
)