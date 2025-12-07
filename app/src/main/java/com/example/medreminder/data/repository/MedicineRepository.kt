package com.example.medreminder.data.repository

import com.example.medreminder.data.dao.MedicineDao
import com.example.medreminder.data.model.Medicine

class MedicineRepository(
    private val dao: MedicineDao
) {

    val medicines = dao.getAllMedicines()

    suspend fun insert(medicine: Medicine): Long {
        return dao.insert(medicine)
    }

    suspend fun delete(medicine: Medicine) {
        dao.delete(medicine)
    }
}
