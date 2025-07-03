package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.model.ActivityPredictionRequest
import com.example.a2zcare.data.model.SensorDataRequest
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.usecases.GetLatestActivityDataUseCase
import com.example.a2zcare.domain.usecases.GetLatestBloodPressurePredictionUseCase
import com.example.a2zcare.domain.usecases.GetLatestHeartDiseasePredictionUseCase
import com.example.a2zcare.domain.usecases.GetLatestHeartRateCalculationUseCase
import com.example.a2zcare.domain.usecases.PredictActivityUseCase
import com.example.a2zcare.domain.usecases.SendBloodPressureAIUseCase
import com.example.a2zcare.domain.usecases.SendHeartDiseaseAIUseCase
import com.example.a2zcare.domain.usecases.SendHeartRateAIUseCase
import com.example.a2zcare.presentation.ui_state_classes.HealthDataUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

@HiltViewModel
class HealthDataViewModel @Inject constructor(
    private val sendBloodPressureAIUseCase: SendBloodPressureAIUseCase,
    private val getLatestBloodPressurePredictionUseCase: GetLatestBloodPressurePredictionUseCase,
    private val sendHeartRateAIUseCase: SendHeartRateAIUseCase,
    private val getLatestHeartRateCalculationUseCase: GetLatestHeartRateCalculationUseCase,
    private val sendHeartDiseaseAIUseCase: SendHeartDiseaseAIUseCase,
    private val getLatestHeartDiseasePredictionUseCase: GetLatestHeartDiseasePredictionUseCase,
    private val getLatestActivityDataUseCase: GetLatestActivityDataUseCase,
    private val predictActivityUseCase: PredictActivityUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthDataUiState())
    val uiState: StateFlow<HealthDataUiState> = _uiState.asStateFlow()

    fun predictActivity(request: ActivityPredictionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = predictActivityUseCase(request)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        activityData = result.data
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun getLatestActivityData() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                when (val result = getLatestActivityDataUseCase(userId)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            activityData = result.data
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun sendBloodPressureAI(batchSize: Int = 10) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val params = SendBloodPressureAIUseCase.Params(userId, batchSize)
                when (val result = sendBloodPressureAIUseCase(params)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            bloodPressureData = result.data
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun getLatestBloodPressurePrediction() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                when (val result = getLatestBloodPressurePredictionUseCase(userId)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            bloodPressureData = result.data
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun sendHeartRateAI(batchSize: Int = 10) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val params = SendHeartRateAIUseCase.Params(userId, batchSize)
                when (val result = sendHeartRateAIUseCase(params)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            heartRateData = result.data
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun getLatestHeartRateCalculation() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                when (val result = getLatestHeartRateCalculationUseCase(userId)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            heartRateData = result.data
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun sendHeartDiseaseAI(batchSize: Int = 10) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val params = SendHeartDiseaseAIUseCase.Params(userId, batchSize)
                when (val result = sendHeartDiseaseAIUseCase(params)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            heartDiseaseData = result.data
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun getLatestHeartDiseasePrediction() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                when (val result = getLatestHeartDiseasePredictionUseCase(userId)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            heartDiseaseData = result.data
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
