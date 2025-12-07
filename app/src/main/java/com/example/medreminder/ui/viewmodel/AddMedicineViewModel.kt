package com.example.medreminder.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medreminder.alarm.AlarmScheduler
import com.example.medreminder.data.database.MedicineDatabase
import com.example.medreminder.data.model.Medicine
import com.example.medreminder.data.repository.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddMedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MedicineRepository
    private val alarmScheduler = AlarmScheduler(application)

    val name = MutableStateFlow("")
    val dose = MutableStateFlow("")
    val hour = MutableStateFlow(8)
    val minute = MutableStateFlow(0)
    val days = MutableStateFlow<List<String>>(emptyList())
    val reminderEnabled = MutableStateFlow(true)
    val quantityLeft = MutableStateFlow("")

    val errorMessage = MutableStateFlow<String?>(null)

    init {
        val dao = MedicineDatabase.getDatabase(application).medicineDao()
        repository = MedicineRepository(dao)
    }

    fun setName(v: String) { name.value = v }
    fun setDose(v: String) { dose.value = v }
    fun setHour(v: Int) { hour.value = v }
    fun setMinute(v: Int) { minute.value = v }
    fun setDays(v: List<String>) { days.value = v }
    fun setReminderEnabled(v: Boolean) { reminderEnabled.value = v }
    fun setQuantityLeft(v: String) { quantityLeft.value = v }

    fun toggleDay(day: String) {
        val current = days.value.toMutableList()
        if (day in current) current.remove(day) else current.add(day)
        days.value = current
    }

    fun save(onSuccess: () -> Unit) {
        if (name.value.isBlank()) {
            errorMessage.value = "El nombre no puede estar vacío"
            return
        }

        if (dose.value.isBlank()) {
            errorMessage.value = "La dosis no puede estar vacía"
            return
        }

        if (days.value.isEmpty()) {
            errorMessage.value = "Selecciona al menos un día"
            return
        }

        viewModelScope.launch {
            val id = repository.insert(
                Medicine(
                    name = name.value,
                    dose = dose.value,
                    hour = hour.value,
                    minute = minute.value,
                    days = days.value,
                    reminderEnabled = reminderEnabled.value,
                    quantityLeft = quantityLeft.value.toIntOrNull()
                )
            )

            if (reminderEnabled.value) {
                alarmScheduler.schedule(
                    medicineId = id.toInt(),
                    name = name.value,
                    hour = hour.value,
                    minute = minute.value,
                    days = days.value
                )
            }

            onSuccess()
        }
    }
}

