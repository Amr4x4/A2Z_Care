package com.example.a2zcare.data.model

data class MedicineItem(
    val id: String,
    val name: String,
    val dosage: String,
    val price: Double,
    val description: String,
    val category: String,
    val inStock: Boolean,
    val prescription: Boolean,
    val manufacturer: String,
    val imageUrl: String
)

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val experience: Int,
    val price: Double,
    val available: Boolean,
    val profileImage: String,
    val education: String,
    val hospital: String,
    val languages: List<String>
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val message: String,
    val timestamp: Long,
    val isFromUser: Boolean,
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, IMAGE, PRESCRIPTION, VOICE_NOTE
}

data class Appointment(
    val id: String,
    val doctorId: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val status: AppointmentStatus,
    val type: AppointmentType,
    val symptoms: String,
    val notes: String
)

enum class AppointmentStatus {
    SCHEDULED, COMPLETED, CANCELLED, IN_PROGRESS
}

enum class AppointmentType {
    VIDEO_CALL, PHONE_CALL, IN_PERSON, CHAT
}

data class HealthRecord(
    val id: String,
    val title: String,
    val date: String,
    val category: String,
    val details: String,
    val doctorName: String,
    val attachments: List<String>
)

data class VitalSigns(
    val date: String,
    val bloodPressure: String,
    val heartRate: Int,
    val temperature: Double,
    val weight: Double,
    val height: Double,
    val bmi: Double
)
