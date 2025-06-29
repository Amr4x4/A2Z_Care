package com.example.a2zcare.domain.usecases

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    private val passwordPattern =
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$")

    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password cannot be empty"
            )
        }

        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password must be at least 8 characters long"
            )
        }

        if (!passwordPattern.matches(password)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
            )
        }

        return ValidationResult(successful = true)
    }
}

