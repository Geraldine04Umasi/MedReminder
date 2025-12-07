package com.example.medreminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.medreminder.ui.AppNavigation
import com.example.medreminder.ui.theme.MedReminderTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        //Pedir permiso para notificaciones
        requestNotificationPermission()



        setContent {
            MedReminderTheme {
                AppNavigation()
            }
        }
    }

    /**
     * Solicitar permiso POST_NOTIFICATIONS en Android 13+
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val permission = Manifest.permission.POST_NOTIFICATIONS

            val alreadyGranted = ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED

            if (!alreadyGranted) {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    /**
     * Launcher moderno para pedir permisos
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Puedes mostrar un mensaje si quieres, pero no es obligatorio
        }

    /**
     * Crear canal de notificaciones
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "med_reminder_channel",
                "Recordatorios de Medicinas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para recordatorios de medicamentos"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
