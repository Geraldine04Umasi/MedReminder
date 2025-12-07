package com.example.medreminder.data.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return if (data.isBlank()) emptyList() else data.split(",")
    }
}
