package com.example.a2zcare.di

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.content.Context

interface WorkerAssistedFactory<T : CoroutineWorker> {
    fun create(context: Context, params: WorkerParameters): T
}
