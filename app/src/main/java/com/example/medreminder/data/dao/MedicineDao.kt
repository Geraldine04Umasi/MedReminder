package com.example.medreminder.data.dao

import androidx.room.*
import com.example.medreminder.data.model.Medicine
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {

    @Insert
    suspend fun insert(medicine: Medicine): Long

    @Delete
    suspend fun delete(medicine: Medicine)

    @Query("SELECT * FROM medicines ORDER BY hour, minute")
    fun getAllMedicines(): Flow<List<Medicine>>
}
