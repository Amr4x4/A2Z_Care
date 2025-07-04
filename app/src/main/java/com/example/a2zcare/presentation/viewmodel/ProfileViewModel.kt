package com.example.a2zcare.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.model.User
import com.example.a2zcare.domain.model.PaymentMethod
import com.example.a2zcare.domain.model.SecuritySettings
import com.example.a2zcare.domain.model.Subscription
import com.example.a2zcare.domain.model.UserPreferences
import com.example.a2zcare.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow(User("", "", "", "", ""))
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

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            userRepository.getUser().collect { _user.value = it }
        }
        viewModelScope.launch {
            userRepository.getUserPreferences().collect { _userPreferences.value = it }
        }
        viewModelScope.launch {
            userRepository.getSecuritySettings().collect { _securitySettings.value = it }
        }
        viewModelScope.launch {
            userRepository.getPaymentMethods().collect { _paymentMethods.value = it }
        }
        viewModelScope.launch {
            userRepository.getSubscriptions().collect { _subscriptions.value = it }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    fun updateUserPreferences(preferences: UserPreferences) {
        viewModelScope.launch {
            userRepository.updateUserPreferences(preferences)
        }
    }

    fun updateSecuritySettings(settings: SecuritySettings) {
        viewModelScope.launch {
            userRepository.updateSecuritySettings(settings)
        }
    }

    fun addPaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            userRepository.addPaymentMethod(paymentMethod)
        }
    }

    fun removePaymentMethod(paymentMethodId: String) {
        viewModelScope.launch {
            userRepository.removePaymentMethod(paymentMethodId)
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
            userRepository.deleteAccount()
            _showDeleteDialog.value = false
        }
    }

    fun deactivateAccount() {
        viewModelScope.launch {
            userRepository.deactivateAccount()
        }
    }
}
