package com.example.medreminder.alarm

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.medreminder.R
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("MED_ALARM", "AlarmReceiver EJECUTADO!!!")

        val name = intent?.getStringExtra("medicine_name") ?: "Tu medicamento"


        val hasPermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            // No mandamos notificación pero tampoco crashea
            return
        }


        val notification = NotificationCompat.Builder(context, "med_reminder_channel")
            .setSmallIcon(R.mipmap.ic_launcher)  // Ícono de la app
            .setContentTitle("Hora de tu medicamento")
            .setContentText("Debes tomar: $name")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), notification)
    }
}
