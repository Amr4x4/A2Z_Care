package com.example.a2zcare.data.repository

import com.example.a2zcare.domain.entities.RunSession
import com.example.a2zcare.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunRepositoryImpl @Inject constructor() : RunRepository {

    private val _currentRunSession = MutableStateFlow<RunSession?>(null)

    override suspend fun startRunSession(): String {
        val sessionId = UUID.randomUUID().toString()
        val session = RunSession(
            id = sessionId,
            startTime = Date(),
            endTime = null,
            distance = 0.0,
            steps = 0,
            caloriesBurned = 0.0,
            route = emptyList(),
            isActive = true
        )
        _currentRunSession.value = session
        return sessionId
    }

    override suspend fun endRunSession(sessionId: String) {
        _currentRunSession.value?.let { session ->
            if (session.id == sessionId) {
                _currentRunSession.value = session.copy(
                    endTime = Date(),
                    isActive = false
                )
            }
        }
    }

    override suspend fun getCurrentRunSession(): Flow<RunSession?> {
        return _currentRunSession.asStateFlow()
    }

    override suspend fun getRunHistory(): Flow<List<RunSession>> {
        return kotlinx.coroutines.flow.flow { emit(emptyList()) }
    }

    override suspend fun updateRunSession(session: RunSession) {
        _currentRunSession.value = session
    }

    fun updateRunSessionData(steps: Int, distance: Double, calories: Double) {
        _currentRunSession.value?.let { session ->
            _currentRunSession.value = session.copy(
                steps = steps,
                distance = distance,
                caloriesBurned = calories
            )
        }
    }
}