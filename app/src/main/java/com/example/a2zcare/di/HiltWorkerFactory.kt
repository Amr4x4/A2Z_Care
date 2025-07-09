package com.example.a2zcare.di

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class HiltWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out CoroutineWorker>, @JvmSuppressWildcards Provider<WorkerAssistedFactory<out CoroutineWorker>>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return try {
            val clazz = Class.forName(workerClassName).asSubclass(CoroutineWorker::class.java)
            val factoryProvider = workerFactories[clazz] ?: return null
            factoryProvider.get().create(appContext, workerParameters)
        } catch (e: Exception) {
            null
        }
    }
}