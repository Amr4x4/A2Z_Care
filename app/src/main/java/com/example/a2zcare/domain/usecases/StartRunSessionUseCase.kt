package com.example.a2zcare.domain.usecases

import com.example.a2zcare.domain.repository.RunRepository
import javax.inject.Inject

class StartRunSessionUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(): String {
        return runRepository.startRunSession()
    }
}