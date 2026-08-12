package com.example.calculadorasleep.data.sleep.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
        val alarmLabel = intent.getStringExtra("ALARM_LABEL") ?: "Alarma de Ciclo"

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("ALARM_LABEL", alarmLabel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}