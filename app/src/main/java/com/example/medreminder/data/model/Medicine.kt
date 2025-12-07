package com.example.medreminder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val dose: String,
    val hour: Int,
    val minute: Int,

    val days: List<String>,
    val reminderEnabled: Boolean,
    val quantityLeft: Int?
)
