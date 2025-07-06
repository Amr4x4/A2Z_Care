package com.example.a2zcare.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.a2zcare.data.model.Appointment
import com.example.a2zcare.data.model.AppointmentStatus
import com.example.a2zcare.data.model.AppointmentType
import com.example.a2zcare.data.model.ChatMessage
import com.example.a2zcare.data.model.Doctor
import com.example.a2zcare.data.model.HealthRecord
import com.example.a2zcare.data.model.MedicineItem
import com.example.a2zcare.data.model.VitalSigns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockDataRepository {

    // Data Models
    data class Medicine(
        val id: String,
        val name: String,
        val company: String,
        val price: Double,
        val originalPrice: Double,
        val category: String,
        val description: String,
        val inStock: Boolean,
        val prescription: Boolean,
        val dosage: String = "",
        val imageUrl: String = ""
    )

    data class PrescriptionMedicine(
        val id: String,
        val name: String,
        val dosage: String,
        val price: Int,
        val company: String = "",
        val description: String = ""
    )

    data class CartItem(
        val medicineId: String,
        val name: String,
        val price: Double,
        val quantity: Int,
        val medicine: Medicine? = null
    )

    data class EmergencyContact(
        val id: String,
        val name: String,
        val phone: String,
        val relation: String,
        val isPrimary: Boolean
    )

    data class Notification(
        val id: String,
        val title: String,
        val message: String,
        val timestamp: Long,
        val isRead: Boolean,
        val type: NotificationType
    )

    enum class NotificationType {
        APPOINTMENT_REMINDER,
        MEDICINE_REFILL,
        HEALTH_TIP,
        EMERGENCY_ALERT,
        PRESCRIPTION_READY,
        DELIVERY_UPDATE
    }

    data class HealthTip(
        val id: String,
        val title: String,
        val description: String,
        val category: String,
        val icon: String,
        val readTime: String = "2 min read"
    )

    // Static Data Lists
    companion object {
        val medicineCategories = listOf(
            "All", "Pain Relief", "Antibiotics", "Vitamins", "Cardiovascular",
            "Diabetes", "Digestive", "Allergy", "Skin Care", "Mental Health",
            "Cold & Flu", "Supplements", "First Aid", "OTC", "Prescription"
        )

        val mockMedicines = listOf(
            Medicine(
                "1", "Paracetamol", "PharmaCorp", 15.99, 19.99, "Pain Relief",
                "Pain relief and fever reducer", true, false, "500mg"
            ),
            Medicine(
                "2", "Ibuprofen", "MediLabs", 12.50, 15.00, "Pain Relief",
                "Anti-inflammatory pain relief", true, false, "400mg"
            ),
            Medicine(
                "3", "Amoxicillin", "BioPharm", 25.00, 30.00, "Antibiotics",
                "Antibiotic for bacterial infections", true, true, "250mg"
            ),
            Medicine(
                "4", "Omeprazole", "GastroMed", 18.75, 22.00, "Digestive",
                "Acid reflux and heartburn treatment", true, false, "20mg"
            ),
            Medicine(
                "5", "Lisinopril", "HeartCare", 22.30, 25.00, "Cardiovascular",
                "Blood pressure medication", true, true, "10mg"
            ),
            Medicine(
                "6", "Metformin", "DiabetesPlus", 19.99, 24.99, "Diabetes",
                "Diabetes medication", true, true, "500mg"
            ),
            Medicine(
                "7", "Cetirizine", "AllergyFree", 14.25, 16.50, "Allergy",
                "Allergy relief medication", true, false, "10mg"
            ),
            Medicine(
                "8", "Vitamin D3", "VitaHealth", 16.50, 19.99, "Vitamins",
                "Bone health supplement", true, false, "1000 IU"
            ),
            Medicine(
                "9", "Aspirin", "CardioMed", 8.99, 10.99, "Cardiovascular",
                "Blood thinner and pain relief", true, false, "81mg"
            ),
            Medicine(
                "10", "Calcium Carbonate", "BoneStrong", 13.75, 16.25, "Vitamins",
                "Bone health supplement", true, false, "500mg"
            ),
            Medicine(
                "11", "Augmentin", "BioPharm", 100.0, 120.0, "Prescription",
                "Antibiotic for infections", true, true, "875mg"
            ),
            Medicine(
                "12", "Bandage Pack", "MediCare", 15.0, 20.0, "First Aid",
                "First aid kit essential", true, false, "Standard"
            ),
            Medicine(
                "13", "Cough Syrup", "MediHealth", 90.0, 110.0, "Cold & Flu",
                "Relief from cough and cold", false, false, "100ml"
            ),
            Medicine(
                "14", "Aloe Vera Gel", "HerbLife", 120.0, 150.0, "Skin Care",
                "Natural skin moisturizer", true, false, "200ml"
            ),
            Medicine(
                "15", "Prednisone", "PharmaCorp", 40.0, 50.0, "Prescription",
                "Anti-inflammatory steroid", true, true, "10mg"
            )
        )

        val mockPrescriptionMedicines = listOf(
            PrescriptionMedicine(
                "p1", "Amoxicillin", "500mg", 60, "BioPharm",
                "Antibiotic for bacterial infections"
            ),
            PrescriptionMedicine(
                "p2", "Prednisone", "10mg", 40, "PharmaCorp",
                "Anti-inflammatory steroid"
            ),
            PrescriptionMedicine(
                "p3", "Metformin", "850mg", 55, "DiabetesPlus",
                "Diabetes management medication"
            ),
            PrescriptionMedicine(
                "p4", "Lisinopril", "20mg", 65, "HeartCare",
                "Blood pressure control medication"
            ),
            PrescriptionMedicine(
                "p5", "Simvastatin", "40mg", 70, "CardioMed",
                "Cholesterol management medication"
            )
        )

        val mockNotifications = listOf(
            Notification(
                "n1", "Medicine Refill Reminder",
                "Time to refill your Lisinopril prescription",
                System.currentTimeMillis() - 3600000, false, NotificationType.MEDICINE_REFILL
            ),
            Notification(
                "n2", "Appointment Reminder",
                "Dr. Johnson appointment tomorrow at 10:00 AM",
                System.currentTimeMillis() - 7200000, true, NotificationType.APPOINTMENT_REMINDER
            ),
            Notification(
                "n3", "Health Tip",
                "Drink at least 8 glasses of water daily for better health",
                System.currentTimeMillis() - 86400000, false, NotificationType.HEALTH_TIP
            ),
            Notification(
                "n4", "Prescription Ready",
                "Your Amoxicillin prescription is ready for pickup",
                System.currentTimeMillis() - 1800000, false, NotificationType.PRESCRIPTION_READY
            ),
            Notification(
                "n5", "Delivery Update",
                "Your VIP medicine order will be delivered in 30 minutes",
                System.currentTimeMillis() - 900000, false, NotificationType.DELIVERY_UPDATE
            )
        )

        val mockHealthTips = listOf(
            HealthTip(
                "ht1", "Stay Hydrated",
                "Drinking adequate water helps maintain body functions and prevents dehydration",
                "General Health", "💧"
            ),
            HealthTip(
                "ht2", "Regular Exercise",
                "30 minutes of daily exercise can improve cardiovascular health and mood",
                "Fitness", "🏃"
            ),
            HealthTip(
                "ht3", "Balanced Diet",
                "Include fruits, vegetables, and whole grains in your daily meals",
                "Nutrition", "🥗"
            ),
            HealthTip(
                "ht4", "Quality Sleep",
                "Getting 7-9 hours of sleep is essential for mental and physical health",
                "Sleep", "😴"
            ),
            HealthTip(
                "ht5", "Stress Management",
                "Practice meditation or deep breathing to manage daily stress",
                "Mental Health", "🧘"
            )
        )

        val mockEmergencyContacts = listOf(
            EmergencyContact("ec1", "Dr. Sarah Johnson", "+1-555-0123", "Primary Doctor", true),
            EmergencyContact("ec2", "City Hospital", "+1-555-0911", "Emergency Room", false),
            EmergencyContact("ec3", "John Smith", "+1-555-0456", "Emergency Contact", false),
            EmergencyContact("ec4", "Poison Control", "+1-800-222-1222", "Poison Control", false),
            EmergencyContact("ec5", "A2Z Care Support", "+1-555-CARE", "Customer Support", false)
        )

        // Mutable cart items for state management
        val cartItems = mutableStateListOf<CartItem>()
    }

    // Flow-based data access methods
    fun getMedicines(): Flow<List<MedicineItem>> = flow {
        emit(
            mockMedicines.map { medicine ->
                MedicineItem(
                    medicine.id,
                    medicine.name,
                    medicine.dosage,
                    medicine.price,
                    medicine.description,
                    medicine.category,
                    medicine.inStock,
                    medicine.prescription,
                    medicine.company,
                    medicine.imageUrl
                )
            }
        )
    }

    fun getMedicinesByCategory(category: String): Flow<List<Medicine>> = flow {
        emit(
            if (category == "All") mockMedicines
            else mockMedicines.filter { it.category == category }
        )
    }

    fun searchMedicines(query: String): Flow<List<Medicine>> = flow {
        emit(
            mockMedicines.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.company.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        )
    }

    fun getDoctors(): Flow<List<Doctor>> = flow {
        emit(
            listOf(
                Doctor(
                    "1", "Dr. Sarah Johnson", "Cardiologist", 4.8, 15, 150.0, true, "",
                    "MD Harvard Medical School", "City General Hospital", listOf("English", "Spanish")
                ),
                Doctor(
                    "2", "Dr. Michael Chen", "Dermatologist", 4.9, 12, 120.0, true, "",
                    "MD Johns Hopkins", "Skin Care Center", listOf("English", "Mandarin")
                ),
                Doctor(
                    "3", "Dr. Emily Rodriguez", "Pediatrician", 4.7, 10, 100.0, true, "",
                    "MD Stanford University", "Children's Hospital", listOf("English", "Spanish")
                ),
                Doctor(
                    "4", "Dr. David Wilson", "Neurologist", 4.9, 20, 200.0, true, "",
                    "MD Mayo Clinic", "Brain & Spine Institute", listOf("English")
                ),
                Doctor(
                    "5", "Dr. Lisa Thompson", "Psychiatrist", 4.6, 8, 180.0, true, "",
                    "MD UCLA", "Mental Health Center", listOf("English", "French")
                ),
                Doctor(
                    "6", "Dr. Robert Kim", "Orthopedic Surgeon", 4.8, 18, 250.0, true, "",
                    "MD Harvard Medical School", "Orthopedic Institute", listOf("English", "Korean")
                ),
                Doctor(
                    "7", "Dr. Amanda Davis", "Gynecologist", 4.7, 14, 160.0, true, "",
                    "MD Columbia University", "Women's Health Clinic", listOf("English")
                ),
                Doctor(
                    "8", "Dr. James Martinez", "Gastroenterologist", 4.8, 16, 170.0, true, "",
                    "MD University of Pennsylvania", "Digestive Health Center", listOf("English", "Spanish")
                )
            )
        )
    }

    fun getChatMessages(doctorId: String): Flow<List<ChatMessage>> = flow {
        emit(
            listOf(
                ChatMessage(
                    "1", "doctor", "Hello! I'm Dr. Johnson. How can I help you today?",
                    System.currentTimeMillis() - 300000, false
                ),
                ChatMessage(
                    "2", "user", "Hi Doctor, I've been experiencing chest pain for the past few days.",
                    System.currentTimeMillis() - 240000, true
                ),
                ChatMessage(
                    "3", "doctor", "I understand your concern. Can you describe the pain? Is it sharp, dull, or burning?",
                    System.currentTimeMillis() - 180000, false
                ),
                ChatMessage(
                    "4", "user", "It's more of a dull ache, and it gets worse when I exercise.",
                    System.currentTimeMillis() - 120000, true
                ),
                ChatMessage(
                    "5", "doctor", "That's important information. How long does the pain last, and have you experienced any shortness of breath?",
                    System.currentTimeMillis() - 60000, false
                ),
                ChatMessage(
                    "6", "user", "The pain lasts about 10-15 minutes, and yes, I do feel short of breath sometimes.",
                    System.currentTimeMillis() - 30000, true
                ),
                ChatMessage(
                    "7", "doctor", "Based on your symptoms, I'd like to schedule you for an ECG. In the meantime, please avoid strenuous activities.",
                    System.currentTimeMillis() - 10000, false
                )
            )
        )
    }

    fun getAppointments(): Flow<List<Appointment>> = flow {
        emit(
            listOf(
                Appointment(
                    "1", "1", "Dr. Sarah Johnson", "Cardiologist", "2024-01-15", "10:00 AM",
                    AppointmentStatus.SCHEDULED, AppointmentType.VIDEO_CALL, "Chest pain", "Follow-up consultation"
                ),
                Appointment(
                    "2", "2", "Dr. Michael Chen", "Dermatologist", "2024-01-18", "2:30 PM",
                    AppointmentStatus.COMPLETED, AppointmentType.IN_PERSON, "Skin rash", "Prescribed topical cream"
                ),
                Appointment(
                    "3", "3", "Dr. Emily Rodriguez", "Pediatrician", "2024-01-20", "11:00 AM",
                    AppointmentStatus.SCHEDULED, AppointmentType.PHONE_CALL, "Child fever", "Routine check-up"
                ),
                Appointment(
                    "4", "4", "Dr. David Wilson", "Neurologist", "2024-01-12", "9:00 AM",
                    AppointmentStatus.CANCELLED, AppointmentType.VIDEO_CALL, "Headaches", "Patient rescheduled"
                ),
                Appointment(
                    "5", "5", "Dr. Lisa Thompson", "Psychiatrist", "2024-01-25", "3:00 PM",
                    AppointmentStatus.SCHEDULED, AppointmentType.CHAT, "Anxiety", "Initial consultation"
                )
            )
        )
    }

    fun getHealthRecords(): Flow<List<HealthRecord>> = flow {
        emit(
            listOf(
                HealthRecord(
                    "1", "Annual Physical Exam", "2024-01-10", "General Health",
                    "Blood pressure: 120/80, Heart rate: 72 bpm, Weight: 70kg",
                    "Dr. Sarah Johnson", listOf("lab_results.pdf", "chest_xray.jpg")
                ),
                HealthRecord(
                    "2", "Skin Consultation", "2024-01-08", "Dermatology",
                    "Examined suspicious mole on back. Biopsy recommended.",
                    "Dr. Michael Chen", listOf("dermatology_report.pdf")
                ),
                HealthRecord(
                    "3", "Blood Test Results", "2024-01-05", "Laboratory",
                    "Cholesterol: 180 mg/dL, Glucose: 95 mg/dL, Vitamin D: 30 ng/mL",
                    "Lab Technician", listOf("blood_test.pdf")
                ),
                HealthRecord(
                    "4", "Vaccination Record", "2024-01-03", "Immunization",
                    "COVID-19 booster vaccine administered",
                    "Nurse Johnson", listOf("vaccination_card.pdf")
                ),
                HealthRecord(
                    "5", "Prescription Refill", "2024-01-01", "Pharmacy",
                    "Lisinopril 10mg refilled for 90 days",
                    "Dr. Sarah Johnson", listOf("prescription.pdf")
                )
            )
        )
    }

    fun getVitalSigns(): Flow<List<VitalSigns>> = flow {
        emit(
            listOf(
                VitalSigns("2024-01-15", "120/80", 72, 36.5, 70.0, 175.0, 22.9),
                VitalSigns("2024-01-10", "118/78", 68, 36.3, 69.8, 175.0, 22.8),
                VitalSigns("2024-01-05", "122/82", 74, 36.7, 70.2, 175.0, 22.9),
                VitalSigns("2024-01-01", "119/79", 70, 36.4, 69.9, 175.0, 22.8),
                VitalSigns("2023-12-25", "121/81", 73, 36.6, 70.1, 175.0, 22.9)
            )
        )
    }

    fun getNotifications(): Flow<List<Notification>> = flow {
        emit(mockNotifications)
    }

    fun getHealthTips(): Flow<List<HealthTip>> = flow {
        emit(mockHealthTips)
    }

    fun getEmergencyContacts(): Flow<List<EmergencyContact>> = flow {
        emit(mockEmergencyContacts)
    }

    fun getPrescriptionMedicines(): Flow<List<PrescriptionMedicine>> = flow {
        emit(mockPrescriptionMedicines)
    }

    // Cart management methods
    fun addToCart(medicine: Medicine, quantity: Int = 1) {
        val existingItem = cartItems.find { it.medicineId == medicine.id }
        if (existingItem != null) {
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            cartItems.add(
                CartItem(
                    medicineId = medicine.id,
                    name = medicine.name,
                    price = medicine.price,
                    quantity = quantity,
                    medicine = medicine
                )
            )
        }
    }

    fun removeFromCart(medicineId: String) {
        cartItems.removeAll { it.medicineId == medicineId }
    }

    fun updateCartItemQuantity(medicineId: String, quantity: Int) {
        val existingItem = cartItems.find { it.medicineId == medicineId }
        if (existingItem != null) {
            if (quantity <= 0) {
                removeFromCart(medicineId)
            } else {
                val index = cartItems.indexOf(existingItem)
                cartItems[index] = existingItem.copy(quantity = quantity)
            }
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getCartTotal(): Double {
        return cartItems.sumOf { it.price * it.quantity }
    }

    fun getCartItemCount(): Int {
        return cartItems.sumOf { it.quantity }
    }

    data class MedicalRecord(
        val id: String,
        val title: String,
        val doctor: String,
        val date: String,
        val type: String,
        val summary: String,
        val attachments: List<String> = emptyList()
    )

    data class LabReport(
        val id: String,
        val testName: String,
        val lab: String,
        val date: String,
        val status: String,
        val results: List<LabResult> = emptyList(),
        val doctorNotes: String = ""
    )

    data class LabResult(
        val parameter: String,
        val value: String,
        val unit: String,
        val referenceRange: String = "",
        val status: String = "Normal"
    )

    data class Prescription(
        val id: String,
        val doctorName: String,
        val date: String,
        val medications: List<Medication>,
        val notes: String = "",
        val validUntil: String = ""
    )

    data class Medication(
        val id: String,
        val name: String,
        val dosage: String,
        val frequency: String,
        val duration: String,
        val instructions: String = ""
    )

    data class Vaccination(
        val id: String,
        val vaccine: String,
        val provider: String,
        val date: String,
        val batchNumber: String,
        val nextDue: String? = null,
        val location: String = "",
        val notes: String = ""
    )

    // Mock Data
    val mockMedicalRecords = listOf(
        MedicalRecord(
            id = "mr1",
            title = "Annual Physical Examination",
            doctor = "Dr. Sarah Johnson",
            date = "2024-01-15",
            type = "General Checkup",
            summary = "Comprehensive physical examination with all vitals normal. Blood pressure: 120/80, Heart rate: 72 bpm, Weight: 70kg. Patient reported no significant health concerns.",
            attachments = listOf("physical_exam_report.pdf", "vital_signs_chart.pdf")
        ),
        MedicalRecord(
            id = "mr2",
            title = "Cardiology Consultation",
            doctor = "Dr. Michael Chen",
            date = "2024-01-10",
            type = "Specialist Visit",
            summary = "Follow-up consultation for chest pain. ECG results normal. Stress test recommended for further evaluation. Patient advised to continue current medication.",
            attachments = listOf("ecg_results.pdf", "consultation_notes.pdf")
        ),
        MedicalRecord(
            id = "mr3",
            title = "Dermatology Screening",
            doctor = "Dr. Emily Rodriguez",
            date = "2024-01-05",
            type = "Screening",
            summary = "Annual skin cancer screening completed. One suspicious mole identified on back. Biopsy scheduled for next week. All other skin lesions appear benign.",
            attachments = listOf("dermatology_report.pdf", "skin_photos.jpg")
        ),
        MedicalRecord(
            id = "mr4",
            title = "Orthopedic Consultation",
            doctor = "Dr. David Wilson",
            date = "2023-12-28",
            type = "Specialist Visit",
            summary = "Evaluation of knee pain. X-rays show mild arthritis. Physical therapy recommended. Patient advised on exercise modifications.",
            attachments = listOf("knee_xray.jpg", "orthopedic_assessment.pdf")
        ),
        MedicalRecord(
            id = "mr5",
            title = "Mental Health Assessment",
            doctor = "Dr. Lisa Thompson",
            date = "2023-12-20",
            type = "Mental Health",
            summary = "Initial consultation for anxiety management. PHQ-9 and GAD-7 assessments completed. Therapy sessions recommended along with lifestyle modifications.",
            attachments = listOf("mental_health_assessment.pdf")
        )
    )

    val mockLabReports = listOf(
        LabReport(
            id = "lr1",
            testName = "Complete Blood Count (CBC)",
            lab = "City Medical Laboratory",
            date = "2024-01-12",
            status = "Normal",
            results = listOf(
                LabResult("Hemoglobin", "14.2", "g/dL", "12.0-16.0", "Normal"),
                LabResult("White Blood Cells", "6.8", "×10³/μL", "4.0-11.0", "Normal"),
                LabResult("Platelets", "285", "×10³/μL", "150-450", "Normal"),
                LabResult("Hematocrit", "42.1", "%", "36.0-48.0", "Normal")
            ),
            doctorNotes = "All values within normal range. No signs of anemia or infection."
        ),
        LabReport(
            id = "lr2",
            testName = "Lipid Panel",
            lab = "HealthCheck Diagnostics",
            date = "2024-01-08",
            status = "Abnormal",
            results = listOf(
                LabResult("Total Cholesterol", "220", "mg/dL", "<200", "High"),
                LabResult("LDL Cholesterol", "145", "mg/dL", "<100", "High"),
                LabResult("HDL Cholesterol", "45", "mg/dL", ">40", "Normal"),
                LabResult("Triglycerides", "180", "mg/dL", "<150", "High")
            ),
            doctorNotes = "Elevated cholesterol levels. Recommend dietary changes and follow-up in 3 months."
        ),
        LabReport(
            id = "lr3",
            testName = "Thyroid Function Test",
            lab = "Metro Lab Services",
            date = "2024-01-03",
            status = "Normal",
            results = listOf(
                LabResult("TSH", "2.5", "μIU/mL", "0.4-4.0", "Normal"),
                LabResult("Free T4", "1.2", "ng/dL", "0.8-1.8", "Normal"),
                LabResult("Free T3", "3.1", "pg/mL", "2.3-4.2", "Normal")
            ),
            doctorNotes = "Thyroid function is normal. No intervention required."
        ),
        LabReport(
            id = "lr4",
            testName = "Diabetes Panel",
            lab = "DiabetesCheck Labs",
            date = "2023-12-30",
            status = "Normal",
            results = listOf(
                LabResult("Fasting Glucose", "92", "mg/dL", "70-99", "Normal"),
                LabResult("HbA1c", "5.4", "%", "<5.7", "Normal"),
                LabResult("Insulin", "8.2", "μIU/mL", "2.6-24.9", "Normal")
            ),
            doctorNotes = "No signs of diabetes. Continue healthy lifestyle."
        ),
        LabReport(
            id = "lr5",
            testName = "Vitamin D Level",
            lab = "NutriTest Laboratory",
            date = "2023-12-25",
            status = "Abnormal",
            results = listOf(
                LabResult("25-Hydroxyvitamin D", "18", "ng/mL", "30-100", "Low")
            ),
            doctorNotes = "Vitamin D deficiency detected. Recommend supplementation."
        )
    )

    val mockPrescriptions = listOf(
        Prescription(
            id = "p1",
            doctorName = "Dr. Sarah Johnson",
            date = "2024-01-15",
            medications = listOf(
                Medication("m1", "Lisinopril", "10mg", "Once daily", "90 days", "Take with food"),
                Medication("m2", "Metformin", "500mg", "Twice daily", "90 days", "Take with meals"),
                Medication("m3", "Aspirin", "81mg", "Once daily", "90 days", "Take with food to prevent stomach upset")
            ),
            notes = "Continue current medications. Follow up in 3 months.",
            validUntil = "2024-04-15"
        ),
        Prescription(
            id = "p2",
            doctorName = "Dr. Michael Chen",
            date = "2024-01-10",
            medications = listOf(
                Medication("m4", "Atorvastatin", "20mg", "Once daily", "30 days", "Take in the evening"),
                Medication("m5", "Omega-3", "1000mg", "Once daily", "30 days", "Take with largest meal")
            ),
            notes = "Started on cholesterol management. Recheck lipid panel in 6 weeks.",
            validUntil = "2024-02-10"
        ),
        Prescription(
            id = "p3",
            doctorName = "Dr. Emily Rodriguez",
            date = "2024-01-05",
            medications = listOf(
                Medication("m6", "Vitamin D3", "2000 IU", "Once daily", "60 days", "Take with fat-containing meal"),
                Medication("m7", "Calcium Carbonate", "500mg", "Twice daily", "60 days", "Take between meals")
            ),
            notes = "For vitamin D deficiency. Recheck levels in 8 weeks.",
            validUntil = "2024-03-05"
        ),
        Prescription(
            id = "p4",
            doctorName = "Dr. David Wilson",
            date = "2023-12-28",
            medications = listOf(
                Medication("m8", "Ibuprofen", "400mg", "3 times daily", "14 days", "Take with food. Maximum 7 days continuous use"),
                Medication("m9", "Glucosamine", "1500mg", "Once daily", "90 days", "Take with meals")
            ),
            notes = "For knee pain management. Physical therapy recommended.",
            validUntil = "2024-01-28"
        ),
        Prescription(
            id = "p5",
            doctorName = "Dr. Lisa Thompson",
            date = "2023-12-20",
            medications = listOf(
                Medication("m10", "Sertraline", "50mg", "Once daily", "30 days", "Take in the morning. May cause drowsiness initially"),
                Medication("m11", "Melatonin", "3mg", "Once daily", "30 days", "Take 30 minutes before bedtime")
            ),
            notes = "For anxiety management. Follow up in 2 weeks to assess tolerance.",
            validUntil = "2024-01-20"
        )
    )

    val mockVaccinations = listOf(
        Vaccination(
            id = "v1",
            vaccine = "COVID-19 Booster (Pfizer)",
            provider = "City Health Department",
            date = "2024-01-10",
            batchNumber = "PF2024A001",
            nextDue = "2024-07-10",
            location = "Left arm",
            notes = "No adverse reactions reported. Patient tolerated well."
        ),
        Vaccination(
            id = "v2",
            vaccine = "Influenza (Flu Shot)",
            provider = "A2Z Care Clinic",
            date = "2023-10-15",
            batchNumber = "FL2023B456",
            nextDue = "2024-10-15",
            location = "Right arm",
            notes = "Annual flu vaccine. Standard dose administered."
        ),
        Vaccination(
            id = "v3",
            vaccine = "Tetanus, Diphtheria, Pertussis (Tdap)",
            provider = "Community Health Center",
            date = "2023-08-20",
            batchNumber = "TD2023C789",
            nextDue = "2033-08-20",
            location = "Left arm",
            notes = "10-year booster. Patient up to date on tetanus protection."
        ),
        Vaccination(
            id = "v4",
            vaccine = "Hepatitis B",
            provider = "Travel Health Clinic",
            date = "2023-06-01",
            batchNumber = "HB2023D012",
            nextDue = null,
            location = "Right arm",
            notes = "Series completed. Immunity confirmed by titer test."
        ),
        Vaccination(
            id = "v5",
            vaccine = "Pneumococcal (PPSV23)",
            provider = "Senior Care Center",
            date = "2023-03-15",
            batchNumber = "PN2023E345",
            nextDue = "2028-03-15",
            location = "Left arm",
            notes = "Recommended for adults over 65. First dose administered."
        ),
        Vaccination(
            id = "v6",
            vaccine = "Shingles (Zoster)",
            provider = "A2Z Care Clinic",
            date = "2023-01-20",
            batchNumber = "ZO2023F678",
            nextDue = "2023-03-20",
            location = "Right arm",
            notes = "First dose of two-dose series. Second dose scheduled."
        ),
        Vaccination(
            id = "v7",
            vaccine = "COVID-19 Primary Series (Pfizer)",
            provider = "Mass Vaccination Site",
            date = "2022-12-01",
            batchNumber = "PF2022G901",
            nextDue = null,
            location = "Left arm",
            notes = "Primary series completed. Booster recommended in 6 months."
        ),
        Vaccination(
            id = "v8",
            vaccine = "Meningococcal (MenACWY)",
            provider = "Travel Health Clinic",
            date = "2022-09-10",
            batchNumber = "MN2022H234",
            nextDue = "2027-09-10",
            location = "Right arm",
            notes = "Required for travel to certain countries. Valid for 5 years."
        )
    )
}
