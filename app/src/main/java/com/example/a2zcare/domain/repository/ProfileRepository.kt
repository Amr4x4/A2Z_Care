package com.example.a2zcare.domain.repository

import com.example.a2zcare.data.model.User
import com.example.a2zcare.domain.model.PaymentMethod
import com.example.a2zcare.domain.model.SecuritySettings
import com.example.a2zcare.domain.model.Subscription
import com.example.a2zcare.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(): Flow<User>
    suspend fun updateUser(user: User)
    suspend fun getUserPreferences(): Flow<UserPreferences>
    suspend fun updateUserPreferences(preferences: UserPreferences)
    suspend fun getSecuritySettings(): Flow<SecuritySettings>
    suspend fun updateSecuritySettings(settings: SecuritySettings)
    suspend fun getPaymentMethods(): Flow<List<PaymentMethod>>
    suspend fun addPaymentMethod(paymentMethod: PaymentMethod)
    suspend fun removePaymentMethod(paymentMethodId: String)
    suspend fun getSubscriptions(): Flow<List<Subscription>>
    suspend fun updateSubscription(subscription: Subscription)
    suspend fun deleteAccount()
    suspend fun deactivateAccount()
}