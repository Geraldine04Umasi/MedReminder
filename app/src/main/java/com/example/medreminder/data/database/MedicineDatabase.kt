package com.example.medreminder.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medreminder.data.dao.MedicineDao
import com.example.medreminder.data.model.Medicine

@Database(entities = [Medicine::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MedicineDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao

    companion object {
        @Volatile
        private var INSTANCE: MedicineDatabase? = null

        fun getDatabase(context: Context): MedicineDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MedicineDatabase::class.java,
                    "medicine_db"
                )
                    // 🔥 NECESARIO para evitar CRASH cuando cambia el schema
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
