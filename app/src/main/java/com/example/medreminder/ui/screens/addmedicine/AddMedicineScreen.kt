package com.example.medreminder.ui.screens.addmedicine

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.medreminder.ui.viewmodel.AddMedicineViewModel
import com.example.medreminder.ui.components.DaysSelector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen(
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AddMedicineViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val ctx = LocalContext.current

    val name by viewModel.name.collectAsState()
    val dose by viewModel.dose.collectAsState()
    val hour by viewModel.hour.collectAsState()
    val minute by viewModel.minute.collectAsState()
    val days by viewModel.days.collectAsState()
    val reminderEnabled by viewModel.reminderEnabled.collectAsState()
    val quantityLeft by viewModel.quantityLeft.collectAsState()

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar medicamento") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("<")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = viewModel::setName,
                label = { Text("Nombre del medicamento") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dose,
                onValueChange = viewModel::setDose,
                label = { Text("Dosis (ej: 1 tableta)") },
                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {
                    TimePickerDialog(
                        ctx,
                        { _, h, m ->
                            viewModel.setHour(h)
                            viewModel.setMinute(m)
                        },
                        hour,
                        minute,
                        false
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar hora: %02d:%02d".format(hour, minute))
            }


            Text("Días de toma:")

            DaysSelector(
                selectedDays = days,
                onChange = viewModel::setDays
            )


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Activar recordatorio")
                Switch(
                    checked = reminderEnabled,
                    onCheckedChange = viewModel::setReminderEnabled
                )
            }


            OutlinedTextField(
                value = quantityLeft?.toString() ?: "",
                onValueChange = { viewModel.setQuantityLeft(it) },
                label = { Text("Cantidad restante (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {
                    viewModel.save {
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}

