package com.example.a2zcare.data.model

data class EmergencyContactRequest(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val userId: String,
    val relationship: String
)

data class EmergencyContact(
    val contactId: Int,
    val name: String,
    val phoneNumber: String,
    val email: String
)

data class UserWithEmergencyContacts(
    val id: String,
    val name: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val emergencyContacts: List<EmergencyContact>
)

data class ConnectContactRequest(
    val userEmail: String,
    val contactId: Int
)
