package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.model.ActivityPredictionRequest
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.model.Result
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    // Heart Disease Prediction State
    private val _heartDiseasePrediction = MutableStateFlow("Normal")
    val heartDiseasePrediction: StateFlow<String> = _heartDiseasePrediction.asStateFlow()

    private val _shouldShowWarningNotification = MutableStateFlow(false)
    val shouldShowWarningNotification: StateFlow<Boolean> =
        _shouldShowWarningNotification.asStateFlow()

    private val _warningMessage = MutableStateFlow("")
    val warningMessage: StateFlow<String> = _warningMessage.asStateFlow()

    private var currentBatchSize = 0
    private var bloodPressureJob: Job? = null
    private var heartRateJob: Job? = null
    private var currentHeartRateBatchSize = 0

    // Heart Disease Monitoring
    private var heartDiseaseJob: Job? = null
    private var currentHeartDiseaseBatchSize = 0

    // Store timestamps of heart disease predictions (NOT heart rate) for the last minute
    private val heartDiseasePredictionTimestamps = mutableListOf<Pair<Long, String>>()

    init {
        startBloodPressureMonitoring()
        startHeartRateMonitoring()
        startHeartDiseaseMonitoring()
    }

    // Heart Disease Monitoring Functions
    fun startHeartDiseaseMonitoring() {
        heartDiseaseJob?.cancel()
        heartDiseaseJob = viewModelScope.launch {
            while (true) {
                sendHeartDiseaseAI(currentHeartDiseaseBatchSize)
                currentHeartDiseaseBatchSize =
                    if (currentHeartDiseaseBatchSize >= 35) 0 else currentHeartDiseaseBatchSize + 1
                delay(1000) // 1 second delay
            }
        }
    }

    fun stopHeartDiseaseMonitoring() {
        heartDiseaseJob?.cancel()
        heartDiseaseJob = null
    }

    private fun checkHeartDiseaseWarning(prediction: String) {
        // Add current prediction to tracking
        val currentTime = System.currentTimeMillis()
        heartDiseasePredictionTimestamps.add(currentTime to prediction)

        // Remove predictions older than 1 minute
        val oneMinuteAgo = currentTime - 60_000
        heartDiseasePredictionTimestamps.removeAll { it.first < oneMinuteAgo }

        // Count occurrences of current prediction type in the last minute
        val countPerMinute = heartDiseasePredictionTimestamps.count { it.second == prediction }

        val shouldWarn = when (prediction) {
            "Supraventricular premature" -> countPerMinute > 6
            "Premature ventricular contraction" -> countPerMinute > 5
            "Fusion of ventricular and normal" -> countPerMinute > 1
            "Unclassifiable" -> countPerMinute > 1
            else -> false // Normal
        }

        if (shouldWarn) {
            _shouldShowWarningNotification.value = true
            _warningMessage.value = getWarningMessage(prediction, countPerMinute)
        }
    }

    private fun getWarningMessage(prediction: String, count: Int): String {
        return when (prediction) {
            "Supraventricular premature" ->
                "⚠️ Heart Alert: Supraventricular Premature beats detected ($count/min). " +
                        "This may indicate irregular heart rhythm. Please avoid caffeine, rest, and consult your doctor if symptoms persist."

            "Premature ventricular contraction" ->
                "⚠️ Heart Alert: Premature Ventricular Contractions detected ($count/min). " +
                        "This may indicate heart rhythm irregularities. Please rest, avoid stimulants, and seek medical attention if you feel chest pain or shortness of breath."

            "Fusion of ventricular and normal" ->
                "⚠️ Heart Alert: Mixed heart rhythm pattern detected ($count/min). " +
                        "This indicates complex heart rhythm changes. Please rest and consult your healthcare provider immediately."

            "Unclassifiable" ->
                "⚠️ Heart Alert: Unusual heart rhythm detected ($count/min). " +
                        "The pattern is unclear and may need medical evaluation. Please rest and contact your doctor for assessment."

            else -> "Heart rhythm appears normal."
        }
    }

    // Get current count for a specific prediction type in the last minute
    fun getCurrentHeartDiseaseCount(predictionType: String): Int {
        val now = System.currentTimeMillis()
        val oneMinuteAgo = now - 60_000

        // Clean old timestamps
        heartDiseasePredictionTimestamps.removeAll { it.first < oneMinuteAgo }

        // Count occurrences of the specific prediction type
        return heartDiseasePredictionTimestamps.count { it.second == predictionType }
    }

    // Get all prediction counts for the last minute (for the card display)
    fun getAllHeartDiseaseCountsLastMinute(): Map<String, Int> {
        val now = System.currentTimeMillis()
        val oneMinuteAgo = now - 60_000

        // Clean old timestamps
        heartDiseasePredictionTimestamps.removeAll { it.first < oneMinuteAgo }

        // Count all prediction types
        val counts = mutableMapOf(
            "Normal" to 0,
            "Supraventricular premature" to 0,
            "Premature ventricular contraction" to 0,
            "Fusion of ventricular and normal" to 0,
            "Unclassifiable" to 0
        )

        heartDiseasePredictionTimestamps.forEach { (_, prediction) ->
            counts[prediction] = counts[prediction]!! + 1
        }

        return counts
    }

    fun dismissWarningNotification() {
        _shouldShowWarningNotification.value = false
        _warningMessage.value = ""
    }

    fun startBloodPressureMonitoring() {
        bloodPressureJob?.cancel()
        bloodPressureJob = viewModelScope.launch {
            while (true) {
                sendBloodPressureAI(currentBatchSize)
                currentBatchSize = if (currentBatchSize >= 35) 0 else currentBatchSize + 1
                delay(60000)
            }
        }
    }

    fun stopBloodPressureMonitoring() {
        bloodPressureJob?.cancel()
        bloodPressureJob = null
    }

    private fun sendBloodPressureAI(batchSize: Int) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                val params = SendBloodPressureAIUseCase.Params(userId, batchSize)
                when (val result = sendBloodPressureAIUseCase(params)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            bloodPressureData = result.data,
                            errorMessage = null
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

    fun startHeartRateMonitoring() {
        heartRateJob?.cancel()
        heartRateJob = viewModelScope.launch {
            while (true) {
                sendHeartRateAI(currentHeartRateBatchSize)
                currentHeartRateBatchSize =
                    if (currentHeartRateBatchSize >= 35) 0 else currentHeartRateBatchSize + 1
                delay(60000)
            }
        }
    }

    fun stopHeartRateMonitoring() {
        heartRateJob?.cancel()
        heartRateJob = null
    }

    fun sendHeartRateAI(batchSize: Int = 10) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                val params = SendHeartRateAIUseCase.Params(userId, batchSize)
                when (val result = sendHeartRateAIUseCase(params)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            heartRateData = result.data,
                            errorMessage = null
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
                val params = SendHeartDiseaseAIUseCase.Params(userId, batchSize)
                when (val result = sendHeartDiseaseAIUseCase(params)) {
                    is Result.Success -> {
                        // Parse the nested prediction from the response
                        val prediction = result.data.prediction
                        _heartDiseasePrediction.value = prediction

                        // Check if warning notification should be shown
                        checkHeartDiseaseWarning(prediction)

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

    override fun onCleared() {
        super.onCleared()
        bloodPressureJob?.cancel()
        heartRateJob?.cancel()
        heartDiseaseJob?.cancel()
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}