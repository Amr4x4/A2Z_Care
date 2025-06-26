package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.RunSession
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    suspend fun startRunSession(): String
    suspend fun endRunSession(sessionId: String)
    suspend fun getCurrentRunSession(): Flow<RunSession?>
    suspend fun getRunHistory(): Flow<List<RunSession>>
    suspend fun updateRunSession(session: RunSession)
}