package com.example.a2zcare.data.repository

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val consultationFee: Double,
    val experience: Int = 0,
    val rating: Float = 0.0f,
    val profileImage: String = "",
    val isAvailable: Boolean = true
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val message: String,
    val timestamp: String,
    val isFromUser: Boolean,
    val messageType: String? = null,
    val prescription: Prescription? = null,
    val report: MedicalReport? = null
)

data class Prescription(
    val id: String,
    val doctorId: String,
    val doctorName: String,
    val medicines: List<PrescriptionMedicine>,
    val instructions: String,
    val date: String
)

data class PrescriptionMedicine(
    val name: String,
    val dosage: String
)

data class MedicalReport(
    val id: String,
    val title: String,
    val summary: String,
    val diagnosis: String,
    val recommendations: String,
    val doctorId: String,
    val doctorName: String,
    val date: String
)

object MockDataRepository2 {
    val mockDoctors = listOf(
        Doctor(
            id = "1",
            name = "Dr. Sarah Ahmed",
            specialty = "Cardiologist",
            consultationFee = 100.0,
            experience = 15,
            rating = 4.8f,
            isAvailable = true
        ),
        Doctor(
            id = "2",
            name = "Dr. Ahmed Ali",
            specialty = "Dermatologist",
            consultationFee = 75.0,
            experience = 12,
            rating = 4.6f,
            isAvailable = true
        ),
        Doctor(
            id = "3",
            name = "Dr. Fatima Hassan",
            specialty = "Pediatrician",
            consultationFee = 80.0,
            experience = 10,
            rating = 4.9f,
            isAvailable = true
        ),
        Doctor(
            id = "4",
            name = "Dr. Omar Mansour",
            specialty = "General Medicine",
            consultationFee = 60.0,
            experience = 8,
            rating = 4.5f,
            isAvailable = false
        )
    )

    val mockChatMessages = mutableListOf(
        ChatMessage(
            id = "1",
            senderId = "1",
            message = "Hello! Good morning. How can I help you today?",
            timestamp = "10:00 AM",
            isFromUser = false
        ),
        ChatMessage(
            id = "2",
            senderId = "user",
            message = "Good morning Doctor. I've been feeling dizzy and experiencing headaches lately.",
            timestamp = "10:01 AM",
            isFromUser = true
        ),
        ChatMessage(
            id = "3",
            senderId = "1",
            message = "I'm sorry to hear that. How long have you been experiencing these symptoms?",
            timestamp = "10:02 AM",
            isFromUser = false
        ),
        ChatMessage(
            id = "4",
            senderId = "user",
            message = "It started about 3 days ago. The headaches are mostly in the morning.",
            timestamp = "10:03 AM",
            isFromUser = true
        ),
        ChatMessage(
            id = "5",
            senderId = "1",
            message = "Thank you for the information. Are you currently taking any medications?",
            timestamp = "10:04 AM",
            isFromUser = false
        )
    )

    val mockPrescriptionMedicines = listOf(
        PrescriptionMedicine(name = "Paracetamol", dosage = "500mg twice daily after meals"),
        PrescriptionMedicine(name = "Ibuprofen", dosage = "400mg three times daily after meals"),
        PrescriptionMedicine(name = "Vitamin D", dosage = "1000 IU daily"),
        PrescriptionMedicine(name = "Aspirin", dosage = "75mg daily before breakfast"),
        PrescriptionMedicine(name = "Omeprazole", dosage = "20mg daily before breakfast"),
        PrescriptionMedicine(name = "Metformin", dosage = "500mg twice daily with meals"),
        PrescriptionMedicine(name = "Amoxicillin", dosage = "500mg three times daily for 7 days"),
        PrescriptionMedicine(name = "Cetirizine", dosage = "10mg once daily at bedtime"),
        PrescriptionMedicine(name = "Salbutamol", dosage = "2 puffs as needed for breathing difficulty"),
        PrescriptionMedicine(name = "Folic Acid", dosage = "5mg daily")
    )

    val mockDoctorResponses = listOf(
        "Thank you for sharing that information. Can you tell me more about your symptoms?",
        "I understand your concern. Based on what you've described, I'd like to ask a few more questions.",
        "That's helpful information. How long have you been experiencing these symptoms?",
        "I see. Are you currently taking any medications or supplements?",
        "Thank you for the details. I recommend we schedule a follow-up consultation.",
        "Based on our discussion, I'll prepare a treatment plan for you.",
        "I want to make sure I understand correctly. Can you describe the pain level on a scale of 1-10?",
        "Your symptoms are concerning. I recommend we run some tests to get a better picture.",
        "Have you noticed any triggers that make the symptoms worse?",
        "Do you have any family history of similar conditions?",
        "I'd like to examine you. Can we schedule a video consultation?",
        "Let me prescribe some medication to help with your symptoms."
    )

    fun currentFormattedDate(): String {
        return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    }

    fun currentFormattedTime(): String {
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    }

    fun generateDoctorResponse(userMessage: String): String {
        // Simple keyword-based response generation
        return when {
            userMessage.lowercase().contains("pain") || userMessage.lowercase().contains("hurt") ->
                "I understand you're experiencing pain. Can you describe the intensity on a scale of 1-10?"
            userMessage.lowercase().contains("headache") ->
                "Headaches can have various causes. How long have you been experiencing them?"
            userMessage.lowercase().contains("fever") ->
                "Fever can indicate infection. Have you measured your temperature?"
            userMessage.lowercase().contains("medicine") || userMessage.lowercase().contains("medication") ->
                "Let me know what medications you're currently taking, including dosages."
            userMessage.lowercase().contains("better") || userMessage.lowercase().contains("good") ->
                "I'm glad to hear you're feeling better. Let's continue monitoring your progress."
            userMessage.lowercase().contains("worse") ->
                "I'm concerned that your symptoms are worsening. We may need to adjust your treatment."
            else -> mockDoctorResponses.random()
        }
    }
}