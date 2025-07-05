package com.example.a2zcare.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.model.User
import com.example.a2zcare.data.remote.request.UpdateUserRequest
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.model.PaymentMethod
import com.example.a2zcare.domain.model.SecuritySettings
import com.example.a2zcare.domain.model.Subscription
import com.example.a2zcare.domain.model.UserPreferences
import com.example.a2zcare.domain.repository.UserRepository
import com.example.a2zcare.domain.usecases.GetUserDataUseCase
import com.example.a2zcare.domain.usecases.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _user = MutableStateFlow(
        User(
            id = "",
            userName = "",
            email = "",
            role = "",
            name = ""
        )
    )
    val user: StateFlow<User> = _user.asStateFlow()

    private val _userPreferences = MutableStateFlow(UserPreferences())
    val userPreferences: StateFlow<UserPreferences> = _userPreferences.asStateFlow()

    private val _securitySettings = MutableStateFlow(SecuritySettings())
    val securitySettings: StateFlow<SecuritySettings> = _securitySettings.asStateFlow()

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods.asStateFlow()

    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptions: StateFlow<List<Subscription>> = _subscriptions.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _updateSuccess = MutableStateFlow<String?>(null)
    val updateSuccess: StateFlow<String?> = _updateSuccess.asStateFlow()

    private val _showLogOutDialog = MutableStateFlow(false)
    val showLogOutDialog: StateFlow<Boolean> = _showLogOutDialog.asStateFlow()

    init {
        loadUserData()
        loadOtherData()
    }

    // Add this to your ProfileViewModel in the loadUserData() function
    fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val cachedUser = tokenManager.getUserData()
                if (cachedUser != null) {
                    // Add logging here
                    Log.d("ProfileViewModel", "Cached user data: $cachedUser")
                    Log.d("ProfileViewModel", "First name: ${cachedUser.firstName}")
                    Log.d("ProfileViewModel", "User ID: ${cachedUser.id}")
                    _user.value = cachedUser
                }

                val userId = tokenManager.getUserId()
                if (!userId.isNullOrEmpty()) {
                    when (val result = getUserDataUseCase.execute(userId)) {
                        is Result.Success -> {
                            // Add logging here too
                            Log.d("ProfileViewModel", "API user data: ${result.data}")
                            Log.d("ProfileViewModel", "API First name: ${result.data.firstName}")
                            Log.d("ProfileViewModel", "API User ID: ${result.data.id}")
                            _user.value = result.data
                            _error.value = null
                        }
                        is Result.Error -> {
                            _error.value = result.message
                        }
                        is Result.Loading -> TODO()
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to load user data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadOtherData() {
        viewModelScope.launch {
            // Load other profile-related data
            userRepository.getUserPreferences().collect {
                _userPreferences.value = it
            }
        }
        viewModelScope.launch {
            userRepository.getSecuritySettings().collect {
                _securitySettings.value = it
            }
        }
        viewModelScope.launch {
            userRepository.getPaymentMethods().collect {
                _paymentMethods.value = it
            }
        }
        viewModelScope.launch {
            userRepository.getSubscriptions().collect {
                _subscriptions.value = it
            }
        }
    }

    fun updateUser(
        firstName: String? = null,
        lastName: String? = null,
        phoneNumber: String? = null,
        address: String? = null,
        age: Int? = null,
        dateOfBirth: String? = null,
        gender: String? = null,
        weightKg: Double? = null,
        height: Int? = null,
        healthGoals: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _updateSuccess.value = null

            try {
                val currentUser = _user.value
                val updateRequest = UpdateUserRequest(
                    id = currentUser.id,
                    firstName = firstName ?: currentUser.firstName,
                    lastName = lastName ?: currentUser.lastName,
                    name = when {
                        firstName != null && lastName != null -> "$firstName $lastName"
                        firstName != null -> firstName
                        else -> currentUser.name
                    },
                    phoneNumber = phoneNumber ?: currentUser.phoneNumber,
                    address = address ?: currentUser.address,
                    age = age ?: currentUser.age,
                    dateOfBirth = dateOfBirth ?: currentUser.dateOfBirth,
                    gender = gender ?: currentUser.gender,
                    weightKg = weightKg ?: currentUser.weightKg,
                    height = height ?: currentUser.height,
                    healthGoals = healthGoals ?: currentUser.healthGoals,
                    updatedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )

                val params = UpdateUserUseCase.Params(currentUser.id, updateRequest)
                when (val result = updateUserUseCase.execute(params)) {
                    is Result.Success -> {
                        // Update local state with the response
                        val updatedUser = currentUser.copy(
                            firstName = result.data.firstName ?: currentUser.firstName,
                            lastName = result.data.lastName ?: currentUser.lastName,
                            name = result.data.name ?: currentUser.name,
                            phoneNumber = result.data.phoneNumber ?: currentUser.phoneNumber,
                            address = result.data.address ?: currentUser.address,
                            age = result.data.age ?: currentUser.age,
                            dateOfBirth = result.data.dateOfBirth ?: currentUser.dateOfBirth,
                            gender = result.data.gender ?: currentUser.gender,
                            weightKg = result.data.weightKg ?: currentUser.weightKg,
                            height = result.data.height?.toInt() ?: currentUser.height,
                            healthGoals = result.data.healthGoals ?: currentUser.healthGoals,
                            updatedAt = result.data.updatedAt ?: currentUser.updatedAt
                        )
                        _user.value = updatedUser

                        // Save updated user data to cache
                        tokenManager.saveUserData(updatedUser)

                        _updateSuccess.value = "Profile updated successfully"
                        _error.value = null
                    }
                    is Result.Error -> {
                        _error.value = result.message
                    }

                    is Result.Loading -> TODO()
                }
            } catch (e: Exception) {
                _error.value = "Failed to update profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUserPreferences(preferences: UserPreferences) {
        viewModelScope.launch {
            try {
                userRepository.updateUserPreferences(preferences)
                _userPreferences.value = preferences
            } catch (e: Exception) {
                _error.value = "Failed to update preferences: ${e.message}"
            }
        }
    }

    fun updateSecuritySettings(settings: SecuritySettings) {
        viewModelScope.launch {
            try {
                userRepository.updateSecuritySettings(settings)
                _securitySettings.value = settings
            } catch (e: Exception) {
                _error.value = "Failed to update security settings: ${e.message}"
            }
        }
    }

    fun addPaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            try {
                userRepository.addPaymentMethod(paymentMethod)
                val currentMethods = _paymentMethods.value.toMutableList()
                currentMethods.add(paymentMethod)
                _paymentMethods.value = currentMethods
            } catch (e: Exception) {
                _error.value = "Failed to add payment method: ${e.message}"
            }
        }
    }

    fun removePaymentMethod(paymentMethodId: String) {
        viewModelScope.launch {
            try {
                userRepository.removePaymentMethod(paymentMethodId)
                val currentMethods = _paymentMethods.value.toMutableList()
                currentMethods.removeAll { it.id == paymentMethodId }
                _paymentMethods.value = currentMethods
            } catch (e: Exception) {
                _error.value = "Failed to remove payment method: ${e.message}"
            }
        }
    }

    fun showDeleteDialog() {
        _showDeleteDialog.value = true
    }

    fun hideDeleteDialog() {
        _showDeleteDialog.value = false
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.deleteAccount()
                _showDeleteDialog.value = false
                // Clear all local data
                tokenManager.clearAllTokens()
            } catch (e: Exception) {
                _error.value = "Failed to delete account: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun showLogOutDialog() {
        _showLogOutDialog.value = true
    }

    fun hideLogOutDialog() {
        _showLogOutDialog.value = false
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearAllTokens()
            _showLogOutDialog.value = false
        }
    }


    fun deactivateAccount() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.deactivateAccount()
            } catch (e: Exception) {
                _error.value = "Failed to deactivate account: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSuccess() {
        _updateSuccess.value = null
    }

    fun refreshUserData() {
        loadUserData()
    }
}