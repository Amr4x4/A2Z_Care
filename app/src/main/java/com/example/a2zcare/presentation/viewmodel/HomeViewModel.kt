package com.example.a2zcare.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.repository.StepRepositoryImpl
import com.example.a2zcare.domain.entities.StepData
import com.example.a2zcare.domain.usecases.GetTodayStepsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val todaySteps: StepData? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayStepsUseCase: GetTodayStepsUseCase,
    private val stepRepository: StepRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTodaySteps()
        simulateStepCounting()
    }

    private fun loadTodaySteps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getTodayStepsUseCase().collect { stepData ->
                _uiState.value = _uiState.value.copy(
                    todaySteps = stepData,
                    isLoading = false
                )
            }
        }
    }

    private fun simulateStepCounting() {
        viewModelScope.launch {
            var steps = 0
            while (true) {
                kotlinx.coroutines.delay(2000)
                steps += (1..5).random()
                stepRepository.updateSteps(steps)
            }
        }
    }
}
