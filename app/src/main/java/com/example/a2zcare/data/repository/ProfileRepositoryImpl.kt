package com.example.a2zcare.data.repository


import com.example.a2zcare.data.model.User
import com.example.a2zcare.domain.model.PaymentMethod
import com.example.a2zcare.domain.model.PaymentType
import com.example.a2zcare.domain.model.SecuritySettings
import com.example.a2zcare.domain.model.Subscription
import com.example.a2zcare.domain.model.UserPreferences
import com.example.a2zcare.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {

    // Mock data - In real app, this would come from database/API
    private var currentUser = User(
        id = "1",
        userName = "Mohamed Gomaa",
        email = "mohamed.gomaa@gmail.com",
        role = "0"
    )

    private var userPreferences = UserPreferences()
    private var securitySettings = SecuritySettings(
        biometricEnabled = true, faceIdEnabled = true, googleAuthEnabled = true
    )

    private val paymentMethods = mutableListOf(
        PaymentMethod(
            "1", PaymentType.PAYPAL, "PayPal", "mohamed.gomaa@gmail.com", isLinked = true
        ),
        PaymentMethod(
            "2", PaymentType.GOOGLE_PAY, "Google Pay", "mohamed.gomaa@gmail.com", isLinked = true
        ),
        PaymentMethod(
            "3", PaymentType.APPLE_PAY, "Apple Pay", "mohamed.gomaa@gmail.com", isLinked = true
        ),
        PaymentMethod(
            "4", PaymentType.MASTERCARD, "Mastercard", lastFourDigits = "4679", isLinked = true
        ),
        PaymentMethod("5", PaymentType.VISA, "Visa", lastFourDigits = "5567", isLinked = true),
        PaymentMethod(
            "6", PaymentType.AMEX, "American Express", lastFourDigits = "8456", isLinked = false
        )
    )

    private val subscriptions = mutableListOf(
        Subscription(
            id = "1", name = "A2Z Care Pro", price = 99.99, duration = "year", features = listOf(
                "Ad-free experience",
                "Advanced monitoring and tracking",
                "Unlimited AI doctor consultations",
                "Advanced data insights",
                "Priority customer support",
                "Early access to new features"
            ), discountPercentage = 16
        )
    )

    override suspend fun getUser(): Flow<User> = flow { emit(currentUser) }

    override suspend fun updateUser(user: User) {
        currentUser = user
    }

    override suspend fun getUserPreferences(): Flow<UserPreferences> =
        flow { emit(userPreferences) }

    override suspend fun updateUserPreferences(preferences: UserPreferences) {
        userPreferences = preferences
    }

    override suspend fun getSecuritySettings(): Flow<SecuritySettings> =
        flow { emit(securitySettings) }

    override suspend fun updateSecuritySettings(settings: SecuritySettings) {
        securitySettings = settings
    }

    override suspend fun getPaymentMethods(): Flow<List<PaymentMethod>> =
        flow { emit(paymentMethods) }

    override suspend fun addPaymentMethod(paymentMethod: PaymentMethod) {
        paymentMethods.add(paymentMethod)
    }

    override suspend fun removePaymentMethod(paymentMethodId: String) {
        paymentMethods.removeIf { it.id == paymentMethodId }
    }

    override suspend fun getSubscriptions(): Flow<List<Subscription>> = flow { emit(subscriptions) }

    override suspend fun updateSubscription(subscription: Subscription) {
        val index = subscriptions.indexOfFirst { it.id == subscription.id }
        if (index != -1) {
            subscriptions[index] = subscription
        }
    }

    override suspend fun deleteAccount() {
        // Implementation for account deletion
    }

    override suspend fun deactivateAccount() {
        // Implementation for account deactivation
    }
}
