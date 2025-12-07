package com.example.medreminder.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medreminder.alarm.AlarmScheduler
import com.example.medreminder.data.database.MedicineDatabase
import com.example.medreminder.data.model.Medicine
import com.example.medreminder.data.repository.MedicineRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MedicineRepository
    private val alarmScheduler = AlarmScheduler(application)

    val medicines: StateFlow<List<Medicine>>

    init {
        val dao = MedicineDatabase.getDatabase(application).medicineDao()
        repository = MedicineRepository(dao)

        medicines = repository.medicines.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch {
            alarmScheduler.cancel(
                medicineId = medicine.id,
                days = medicine.days
            )

            repository.delete(medicine)
        }
    }
}
