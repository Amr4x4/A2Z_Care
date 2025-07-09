package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.local.entity.Medicine
import com.example.a2zcare.data.model.NextMedicineInfo
import com.example.a2zcare.domain.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val repository: MedicineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicineUiState())
    val uiState: StateFlow<MedicineUiState> = _uiState.asStateFlow()

    val activeMedicines = repository.getActiveMedicines()
    val finishedMedicines = repository.getFinishedMedicines()

    private val _nextMedicine = MutableStateFlow<NextMedicineInfo?>(null)
    val nextMedicine: StateFlow<NextMedicineInfo?> = _nextMedicine.asStateFlow()

    init {
        viewModelScope.launch {
            activeMedicines.collect { medicines ->
                _uiState.value = _uiState.value.copy(activeMedicines = medicines)
            }
        }

        viewModelScope.launch {
            while (true) {
                _nextMedicine.value = repository.getNextMedicine()
                delay(60_000) // Update every 1 minute
            }
        }
    }

    fun addMedicine(medicine: Medicine) {
        viewModelScope.launch {
            repository.insertMedicine(medicine)
        }
    }

    fun updateMedicine(medicine: Medicine) {
        viewModelScope.launch {
            repository.updateMedicine(medicine)
        }
    }

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch {
            repository.deleteMedicine(medicine)
        }
    }

    fun takeMedicine(medicineId: String, scheduledTime: String) {
        viewModelScope.launch {
            repository.takeMedicine(medicineId, scheduledTime)
        }
    }

    fun getMedicineHistory(medicineId: String) = repository.getMedicineHistory(medicineId)

    fun setLoading(loading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = loading)
    }

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
    }
}

data class MedicineUiState(
    val activeMedicines: List<Medicine> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
