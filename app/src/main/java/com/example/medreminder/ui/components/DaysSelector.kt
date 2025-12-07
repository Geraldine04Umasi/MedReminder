package com.example.medreminder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun DaysSelector(
    selectedDays: List<String>,
    onChange: (List<String>) -> Unit
) {
    val days = listOf(
        "Lunes", "Martes", "Miércoles",
        "Jueves", "Viernes", "Sábado", "Domingo"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        days.chunked(3).forEach { rowDays ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowDays.forEach { day ->
                    val isSelected = selectedDays.contains(day)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                val updated = if (isSelected) {
                                    selectedDays - day
                                } else {
                                    selectedDays + day
                                }
                                onChange(updated)
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.take(3), // "Lun", "Mar", "Mié"...
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }
}
