package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.domain.repository.StepTrackerRepository
import com.example.a2zcare.presentation.navegation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainNavigationViewModel @Inject constructor(
    private val repository: StepTrackerRepository
) : ViewModel() {

    private val _navigationState = MutableStateFlow(NavigationState())
    val navigationState: StateFlow<NavigationState> = _navigationState.asStateFlow()

    fun checkInitialDestination() {
        viewModelScope.launch {
            try {
                val userProfile = repository.getUserProfile()
                val destination = if (userProfile != null) {
                    Screen.Home.route
                } else {
                    Screen.OnBoarding.route
                }

                _navigationState.value = _navigationState.value.copy(
                    initialDestination = destination
                )
            } catch (e: Exception) {
                _navigationState.value = _navigationState.value.copy(
                    initialDestination = Screen.OnBoarding.route
                )
            }
        }
    }
}

data class NavigationState(
    val initialDestination: String? = null
)