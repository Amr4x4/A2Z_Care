package com.example.a2zcare.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.onboarding.DataStoreRepository
import com.example.a2zcare.presentation.navegation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val startDestination = mutableStateOf(Screen.OnBoarding.route)

    init {
        viewModelScope.launch {
            repository.readOnBoardingState().collect { completed ->
                startDestination.value = if (completed) {
                    Screen.GetStart.route
                } else {
                    Screen.OnBoarding.route
                }
                _isLoading.value = false
            }
        }
    }
}
