package com.example.medreminder.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    // ---------------------------------------------------------------
    // PROGRAMAR ALARMAS
    // ---------------------------------------------------------------
    fun schedule(medicineId: Int, name: String, hour: Int, minute: Int, days: List<String>) {

        val dayMap = mapOf(
            "Lunes" to Calendar.MONDAY,
            "Martes" to Calendar.TUESDAY,
            "Miércoles" to Calendar.WEDNESDAY,
            "Jueves" to Calendar.THURSDAY,
            "Viernes" to Calendar.FRIDAY,
            "Sábado" to Calendar.SATURDAY,
            "Domingo" to Calendar.SUNDAY
        )

        days.forEach { day ->

            val dow = dayMap[day] ?: return@forEach

            val calendar = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, dow)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                // Si ya pasó hoy → próxima semana
                if (before(Calendar.getInstance())) {
                    add(Calendar.WEEK_OF_YEAR, 1)
                }
            }

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("medicine_name", name)
            }

            val requestCode = generateRequestCode(medicineId, day)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,  // cada semana
                pendingIntent
            )
        }
    }


    // ---------------------------------------------------------------
    // CANCELAR ALARMAS
    // ---------------------------------------------------------------
    fun cancel(medicineId: Int, days: List<String>) {

        days.forEach { day ->
            val requestCode = generateRequestCode(medicineId, day)

            val intent = Intent(context, AlarmReceiver::class.java)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
            )

            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    // ---------------------------------------------------------------
    // REQUEST CODE ÚNICO (medicina + día)
    // ---------------------------------------------------------------
    private fun generateRequestCode(id: Int, day: String): Int {
        return id * 100 + day.hashCode()
    }
}

