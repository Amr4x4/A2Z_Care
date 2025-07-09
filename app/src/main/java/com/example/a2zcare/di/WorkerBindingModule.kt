package com.example.a2zcare.di

import androidx.work.CoroutineWorker
import com.example.a2zcare.service.MedicineNotificationWorker
import com.example.a2zcare.service.MedicineReminderWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out CoroutineWorker>)

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(MedicineReminderWorker::class)
    abstract fun bindReminderWorkerFactory(
        factory: MedicineReminderWorker.Factory
    ): WorkerAssistedFactory<out CoroutineWorker>

    @Binds
    @IntoMap
    @WorkerKey(MedicineNotificationWorker::class)
    abstract fun bindMedicineNotificationWorkerFactory(
        factory: MedicineNotificationWorker.Factory
    ): WorkerAssistedFactory<out CoroutineWorker>
}