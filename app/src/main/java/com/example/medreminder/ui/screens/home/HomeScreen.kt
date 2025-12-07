package com.example.medreminder.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medreminder.data.model.Medicine
import com.example.medreminder.ui.viewmodel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddMedicineClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val medicines by viewModel.medicines.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Medicamentos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMedicineClick) {
                Text("Nuevo recordatorio")
            }
        }
    ) { padding ->

        if (medicines.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no tienes medicamentos registrados.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(medicines) { med ->
                    MedicineItem(
                        medicine = med,
                        onDelete = { viewModel.deleteMedicine(med) }
                    )
                }
            }
        }
    }
}


// ----------------------------------------------------------------------
// Tarjeta de lista para cada medicamento
// ----------------------------------------------------------------------
@Composable
fun MedicineItem(
    medicine: Medicine,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = medicine.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatTime(medicine.hour, medicine.minute),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


// ----------------------------------------------------------------------
// Función para formatear hora
// ----------------------------------------------------------------------
fun formatTime(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    return "%02d:%02d %s".format(hour12, minute, amPm)
}
