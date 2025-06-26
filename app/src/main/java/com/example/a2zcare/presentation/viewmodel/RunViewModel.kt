package com.example.a2zcare.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.repository.RunRepositoryImpl
import com.example.a2zcare.domain.entities.RunSession
import com.example.a2zcare.domain.usecases.StartRunSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

data class RunUiState(
    val currentSession: RunSession? = null,
    val isRunning: Boolean = false,
    val elapsedTime: Long = 0L
)

@HiltViewModel
class RunViewModel @Inject constructor(
    private val startRunSessionUseCase: StartRunSessionUseCase,
    private val runRepository: RunRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(RunUiState())
    val uiState: StateFlow<RunUiState> = _uiState.asStateFlow()

    private var startTime = 0L

    init {
        observeCurrentSession()
    }

    private fun observeCurrentSession() {
        viewModelScope.launch {
            runRepository.getCurrentRunSession().collect { session ->
                _uiState.value = _uiState.value.copy(
                    currentSession = session,
                    isRunning = session?.isActive == true
                )
            }
        }
    }

    fun startRun() {
        viewModelScope.launch {
            startRunSessionUseCase()
            startTime = System.currentTimeMillis()
            _uiState.value = _uiState.value.copy(isRunning = true)
            startTimer()
            simulateRunData()
        }
    }

    fun stopRun() {
        viewModelScope.launch {
            _uiState.value.currentSession?.let { session ->
                runRepository.endRunSession(session.id)
                _uiState.value = _uiState.value.copy(isRunning = false)
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_uiState.value.isRunning) {
                val elapsed = System.currentTimeMillis() - startTime
                _uiState.value = _uiState.value.copy(elapsedTime = elapsed)
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    private fun simulateRunData() {
        viewModelScope.launch {
            var steps = 0
            var distance = 0.0
            while (_uiState.value.isRunning) {
                kotlinx.coroutines.delay(1000)
                steps += (2..8).random()
                distance += Random.nextDouble(0.005, 0.02)
                val calories = steps * 0.05
                runRepository.updateRunSessionData(steps, distance, calories)
            }
        }
    }
}
