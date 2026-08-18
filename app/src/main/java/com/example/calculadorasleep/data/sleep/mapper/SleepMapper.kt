package com.example.calculadorasleep.data.sleep.mapper

import com.example.calculadorasleep.data.sleep.local.sleep.SleepEntity
import com.example.calculadorasleep.domain.sleep.model.Sleep
import okhttp3.internal.userAgent

fun SleepEntity.toDomain(): Sleep = Sleep(
    sleepId = sleepId,
    dormirTiempo = dormirTiempo,
    despertarTiempo = despertarTiempo,
    ciclos = ciclos,
    calidadSleep = calidadSleep
)

fun Sleep.toEntity(uid: String): SleepEntity = SleepEntity(
    sleepId = sleepId,
    userId = uid,
    dormirTiempo = dormirTiempo,
    despertarTiempo = despertarTiempo,
    ciclos = ciclos,
    calidadSleep = calidadSleep
)