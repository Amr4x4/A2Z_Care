package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.model.EmergencyContact2
import com.example.a2zcare.data.model.VitalSigns2
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.model.EmergencyConstants
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.domain.usecases.*
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
class EnhancedHealthDataViewModel @Inject constructor(
    private val sendBloodPressureAIUseCase: SendBloodPressureAIUseCase,
    private val getLatestBloodPressurePredictionUseCase: GetLatestBloodPressurePredictionUseCase,
    private val sendHeartRateAIUseCase: SendHeartRateAIUseCase,
    private val getLatestHeartRateCalculationUseCase: GetLatestHeartRateCalculationUseCase,
    private val sendHeartDiseaseAIUseCase: SendHeartDiseaseAIUseCase,
    private val getLatestHeartDiseasePredictionUseCase: GetLatestHeartDiseasePredictionUseCase,
    private val getLatestActivityDataUseCase: GetLatestActivityDataUseCase,
    private val predictActivityUseCase: PredictActivityUseCase,
    private val checkCriticalVitalsUseCase: CheckCriticalVitalsUseCase,
    private val emergencyCallService: EmergencyCallService,
    private val triggerEmergencyUseCase: TriggerEmergencyUseCase, // ✅ Injected properly
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthDataUiState())
    val uiState: StateFlow<HealthDataUiState> = _uiState.asStateFlow()

    private val _isEmergencyCountdownActive = MutableStateFlow(false)
    val isEmergencyCountdownActive: StateFlow<Boolean> = _isEmergencyCountdownActive.asStateFlow()

    private val _emergencyCountdown = MutableStateFlow(0)
    val emergencyCountdown: StateFlow<Int> = _emergencyCountdown.asStateFlow()

    private val _emergencyReason = MutableStateFlow("")
    val emergencyReason: StateFlow<String> = _emergencyReason.asStateFlow()

    private val _currentVitalSigns = MutableStateFlow<VitalSigns2?>(null)
    val currentVitalSigns: StateFlow<VitalSigns2?> = _currentVitalSigns.asStateFlow()

    private val _emergencyContacts = MutableStateFlow<List<EmergencyContact2>>(emptyList())
    val emergencyContacts: StateFlow<List<EmergencyContact2>> = _emergencyContacts.asStateFlow()

    private val _heartDiseasePrediction = MutableStateFlow("Normal")
    val heartDiseasePrediction: StateFlow<String> = _heartDiseasePrediction.asStateFlow()

    private val _shouldShowWarningNotification = MutableStateFlow(false)
    val shouldShowWarningNotification: StateFlow<Boolean> = _shouldShowWarningNotification.asStateFlow()

    private val _warningMessage = MutableStateFlow("")
    val warningMessage: StateFlow<String> = _warningMessage.asStateFlow()

    private var currentBatchSize = 0
    private var bloodPressureJob: Job? = null
    private var heartRateJob: Job? = null
    private var currentHeartRateBatchSize = 0
    private var heartDiseaseJob: Job? = null
    private var currentHeartDiseaseBatchSize = 0
    private var emergencyCountdownJob: Job? = null

    private val heartDiseasePredictionTimestamps = mutableListOf<Pair<Long, String>>()

    init {
        startBloodPressureMonitoring()
        startHeartRateMonitoring()
        startHeartDiseaseMonitoring()
        loadEmergencyContacts()
    }

    fun saveContact(contact: EmergencyContact2) {
        viewModelScope.launch {
            val updated = _emergencyContacts.value.toMutableList().apply {
                add(contact)
            }.map {
                if (contact.isPrimary && it.id != contact.id) it.copy(isPrimary = false) else it
            }
            _emergencyContacts.value = updated
        }
    }

    fun updateContact(contact: EmergencyContact2) {
        viewModelScope.launch {
            val currentContacts = _emergencyContacts.value.toMutableList()
            val index = currentContacts.indexOfFirst { it.id == contact.id }
            if (index != -1) {
                currentContacts[index] = contact
                val updated = currentContacts.map {
                    if (contact.isPrimary && it.id != contact.id) it.copy(isPrimary = false) else it
                }
                _emergencyContacts.value = updated
            }
        }
    }

    fun deleteContact(contactId: Int) {
        viewModelScope.launch {
            _emergencyContacts.value = _emergencyContacts.value.filter { it.id != contactId }
        }
    }

    fun callContact(phoneNumber: String) {
        viewModelScope.launch {
            try {
                emergencyCallService.callContact(phoneNumber)
                _uiState.value = _uiState.value.copy(successMessage = "Calling $phoneNumber...")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to make call: ${e.message}")
            }
        }
    }

    private fun loadEmergencyContacts() {
        viewModelScope.launch {
            _emergencyContacts.value = listOf(
                EmergencyContact2(1, "John Doe", "+1234567890", "john@example.com", "Father", true),
                EmergencyContact2(2, "Jane Smith", "+0987654321", "jane@example.com", "Mother", false)
            )
        }
    }

    fun checkForEmergencyCondition(systolicBP: Int, diastolicBP: Int, heartRate: Int) {
        val vitals = VitalSigns2(systolicBP, diastolicBP, heartRate)
        _currentVitalSigns.value = vitals

        viewModelScope.launch {
            when (val result = checkCriticalVitalsUseCase(vitals)) {
                is CriticalVitalsResult.Critical -> {
                    _emergencyReason.value = result.reason
                    _emergencyContacts.value = result.contacts
                    startEmergencyCountdown()
                }
                is CriticalVitalsResult.Normal -> {}
            }
        }
    }

    private fun startEmergencyCountdown() {
        if (_isEmergencyCountdownActive.value) return

        _isEmergencyCountdownActive.value = true
        _emergencyCountdown.value = EmergencyConstants.EMERGENCY_COUNTDOWN_SECONDS

        emergencyCountdownJob = viewModelScope.launch {
            repeat(EmergencyConstants.EMERGENCY_COUNTDOWN_SECONDS) {
                delay(1000)
                _emergencyCountdown.value -= 1
            }

            if (_emergencyCountdown.value <= 0) {
                triggerEmergencyActions()
            }
        }
    }

    fun cancelEmergencyCountdown() {
        emergencyCountdownJob?.cancel()
        _isEmergencyCountdownActive.value = false
        _emergencyCountdown.value = 0
        _emergencyReason.value = ""
    }

    private fun triggerEmergencyActions() {
        viewModelScope.launch {
            val vitals = _currentVitalSigns.value
            val contacts = _emergencyContacts.value
            val userId = tokenManager.getUserId()

            if (vitals != null && contacts.isNotEmpty() && userId != null) {
                try {
                    triggerEmergencyUseCase(vitals, contacts)
                    emergencyCallService.callAmbulance()
                    _uiState.value = _uiState.value.copy(successMessage = "Emergency services have been contacted")
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(errorMessage = "Failed to trigger emergency: ${e.message}")
                }
            }

            _isEmergencyCountdownActive.value = false
            _emergencyCountdown.value = 0
        }
    }

    private fun sendBloodPressureAI(batchSize: Int) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId() ?: return@launch
            val params = SendBloodPressureAIUseCase.Params(userId, batchSize)

            when (val result = sendBloodPressureAIUseCase(params)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        bloodPressureData = result.data,
                        errorMessage = null
                    )

                    val systolic = result.data?.systolic?.toInt() ?: 0
                    val diastolic = result.data?.diastolic?.toInt() ?: 0
                    val heartRate = _uiState.value.heartRateData?.heartrate ?: 0

                    if (systolic > 0 && diastolic > 0) {
                        checkForEmergencyCondition(systolic, diastolic, heartRate)
                    }
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun sendHeartRateAI(batchSize: Int = 10) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId() ?: return@launch
            val params = SendHeartRateAIUseCase.Params(userId, batchSize)

            when (val result = sendHeartRateAIUseCase(params)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        heartRateData = result.data,
                        errorMessage = null
                    )

                    val heartRate = result.data?.heartrate ?: 0
                    val systolic = _uiState.value.bloodPressureData?.systolic?.toInt() ?: 0
                    val diastolic = _uiState.value.bloodPressureData?.diastolic?.toInt() ?: 0

                    if (heartRate > 0) {
                        checkForEmergencyCondition(systolic, diastolic, heartRate)
                    }
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun startBloodPressureMonitoring() {
        bloodPressureJob?.cancel()
        bloodPressureJob = viewModelScope.launch {
            while (true) {
                sendBloodPressureAI(currentBatchSize)
                currentBatchSize = (currentBatchSize + 1) % 36
                delay(60000)
            }
        }
    }

    fun stopBloodPressureMonitoring() {
        bloodPressureJob?.cancel()
        bloodPressureJob = null
    }

    fun startHeartRateMonitoring() {
        heartRateJob?.cancel()
        heartRateJob = viewModelScope.launch {
            while (true) {
                sendHeartRateAI(currentHeartRateBatchSize)
                currentHeartRateBatchSize = (currentHeartRateBatchSize + 1) % 36
                delay(60000)
            }
        }
    }

    fun stopHeartRateMonitoring() {
        heartRateJob?.cancel()
        heartRateJob = null
    }

    fun startHeartDiseaseMonitoring() {
        heartDiseaseJob?.cancel()
        heartDiseaseJob = viewModelScope.launch {
            while (true) {
                sendHeartDiseaseAI(currentHeartDiseaseBatchSize)
                currentHeartDiseaseBatchSize = (currentHeartDiseaseBatchSize + 1) % 36
                delay(1000)
            }
        }
    }

    fun stopHeartDiseaseMonitoring() {
        heartDiseaseJob?.cancel()
        heartDiseaseJob = null
    }

    private fun checkHeartDiseaseWarning(prediction: String) {
        val now = System.currentTimeMillis()
        heartDiseasePredictionTimestamps.add(now to prediction)
        heartDiseasePredictionTimestamps.removeAll { it.first < now - 60000 }

        val count = heartDiseasePredictionTimestamps.count { it.second == prediction }

        val shouldWarn = when (prediction) {
            "Supraventricular premature" -> count > 6
            "Premature ventricular contraction" -> count > 5
            "Fusion of ventricular and normal" -> count > 1
            "Unclassifiable" -> count > 1
            else -> false
        }

        if (shouldWarn) {
            _shouldShowWarningNotification.value = true
            _warningMessage.value = getWarningMessage(prediction, count)
        }
    }

    private fun getWarningMessage(prediction: String, count: Int): String {
        return when (prediction) {
            "Supraventricular premature" ->
                "⚠️ Supraventricular Premature beats detected ($count/min). Avoid caffeine. Consult doctor if symptoms persist."
            "Premature ventricular contraction" ->
                "⚠️ Premature Ventricular Contractions detected ($count/min). Seek medical help if symptoms occur."
            "Fusion of ventricular and normal" ->
                "⚠️ Mixed heart rhythm pattern detected. Please rest and consult your healthcare provider immediately."
            "Unclassifiable" ->
                "⚠️ Unusual heart rhythm detected. Contact your doctor for assessment."
            else -> "Heart rhythm appears normal."
        }
    }

    fun sendHeartDiseaseAI(batchSize: Int = 10) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId() ?: return@launch
            val params = SendHeartDiseaseAIUseCase.Params(userId, batchSize)

            when (val result = sendHeartDiseaseAIUseCase(params)) {
                is Result.Success -> {
                    val prediction = result.data.prediction
                    _heartDiseasePrediction.value = prediction
                    checkHeartDiseaseWarning(prediction)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        heartDiseaseData = result.data
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun dismissWarningNotification() {
        _shouldShowWarningNotification.value = false
        _warningMessage.value = ""
    }

    fun getCurrentHeartDiseaseCount(type: String): Int {
        val now = System.currentTimeMillis()
        heartDiseasePredictionTimestamps.removeAll { it.first < now - 60000 }
        return heartDiseasePredictionTimestamps.count { it.second == type }
    }

    fun getAllHeartDiseaseCountsLastMinute(): Map<String, Int> {
        val now = System.currentTimeMillis()
        heartDiseasePredictionTimestamps.removeAll { it.first < now - 60000 }

        val types = listOf(
            "Normal", "Supraventricular premature", "Premature ventricular contraction",
            "Fusion of ventricular and normal", "Unclassifiable"
        )

        return types.associateWith { type ->
            heartDiseasePredictionTimestamps.count { it.second == type }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }

    override fun onCleared() {
        super.onCleared()
        bloodPressureJob?.cancel()
        heartRateJob?.cancel()
        heartDiseaseJob?.cancel()
        emergencyCountdownJob?.cancel()
    }
}
