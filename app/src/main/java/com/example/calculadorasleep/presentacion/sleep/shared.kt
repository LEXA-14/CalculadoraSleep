package com.example.calculadorasleep.presentacion.sleep

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

object SleepTimeUtils {


    fun toLocalTime(hour: Int, minute: Int, isAm: Boolean): LocalTime {
        val hour24 = when {
            isAm && hour == 12 -> 0
            !isAm && hour != 12 -> hour + 12
            else -> hour
        }
        return LocalTime.of(hour24, minute)
    }


    fun toHourMinutePeriod(time: LocalTime): Triple<Int, Int, Boolean> {
        val isAm = time.hour < 12
        val hour12 = when {
            time.hour == 0 -> 12
            time.hour > 12 -> time.hour - 12
            else -> time.hour
        }
        return Triple(hour12, time.minute, isAm)
    }
}