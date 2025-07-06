package com.example.a2zcare.presentation.screens.vip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VIPDoctorConsultationsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Available", "Upcoming", "History")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VIP Doctor Consultations", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // VIP Consultation Banner
            VIPConsultationBanner()

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> AvailableDoctorsContent(navController)
                1 -> UpcomingConsultationsContent()
                2 -> ConsultationHistoryContent()
            }
        }
    }
}

@Composable
private fun VIPConsultationBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.VideoCall,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "VIP Doctor Consultations",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "24/7 availability • Expert doctors • Instant consultations",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun AvailableDoctorsContent(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Specialties Filter
        item {
            Text(
                "Specialties",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(doctorSpecialties) { specialty ->
                    FilterChip(
                        onClick = { /* Filter by specialty */ },
                        label = { Text(specialty) },
                        selected = false
                    )
                }
            }
        }

        // Available Doctors
        item {
            Text(
                "Available Doctors",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(mockDoctors) { doctor ->
            DoctorCard(
                doctor = doctor,
                onConsultNow = { navController.navigate("chat/${doctor.id}") },
                onBookAppointment = { /* Book appointment */ }
            )
        }
    }
}

@Composable
private fun UpcomingConsultationsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mockUpcomingConsultations) { consultation ->
            UpcomingConsultationCard(consultation = consultation)
        }
    }
}

@Composable
private fun ConsultationHistoryContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mockConsultationHistory) { consultation ->
            ConsultationHistoryCard(consultation = consultation)
        }
    }
}

@Composable
private fun DoctorCard(
    doctor: Doctor,
    onConsultNow: () -> Unit,
    onBookAppointment: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF2196F3).copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        doctor.name.take(2).uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        doctor.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        doctor.specialty,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        "${doctor.experience} years experience",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            doctor.rating.toString(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    if (doctor.isAvailable) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                        Text(
                            "Available",
                            fontSize = 10.sp,
                            color = Color(0xFF4CAF50)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFFE53935), CircleShape)
                        )
                        Text(
                            "Busy",
                            fontSize = 10.sp,
                            color = Color(0xFFE53935)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Consultation Fee: $${doctor.consultationFee}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onBookAppointment,
                    modifier = Modifier.weight(1f),
                    enabled = doctor.isAvailable
                ) {
                    Text("Book Appointment")
                }

                Button(
                    onClick = onConsultNow,
                    modifier = Modifier.weight(1f),
                    enabled = doctor.isAvailable,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    )
                ) {
                    Text("Consult Now")
                }
            }
        }
    }
}

@Composable
private fun UpcomingConsultationCard(consultation: UpcomingConsultation) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        consultation.doctorName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        consultation.specialty,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        "${consultation.date} at ${consultation.time}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        consultation.type,
                        fontSize = 12.sp,
                        color = Color(0xFF2196F3),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "$${consultation.fee}",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Reschedule */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reschedule")
                }

                Button(
                    onClick = { /* Join Call */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Join Call")
                }
            }
        }
    }
}

@Composable
private fun ConsultationHistoryCard(consultation: ConsultationHistory) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        consultation.doctorName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        consultation.specialty,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        consultation.date,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        consultation.status,
                        fontSize = 12.sp,
                        color = when (consultation.status) {
                            "Completed" -> Color(0xFF4CAF50)
                            "Cancelled" -> Color(0xFFE53935)
                            else -> Color.Gray
                        },
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "$${consultation.fee}",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (consultation.diagnosis.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Diagnosis: ${consultation.diagnosis}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            if (consultation.prescription.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Prescription: ${consultation.prescription}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* View Details */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Details")
                }

                Button(
                    onClick = { /* Download Report */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Download Report")
                }
            }
        }
    }
}

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val experience: Int,
    val rating: Double,
    val consultationFee: Double,
    val isAvailable: Boolean
)

data class UpcomingConsultation(
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val fee: Double,
    val type: String // e.g., "Video", "In-Person"
)

data class ConsultationHistory(
    val doctorName: String,
    val specialty: String,
    val date: String,
    val status: String, // e.g., "Completed", "Cancelled"
    val fee: Double,
    val diagnosis: String,
    val prescription: String
)

val doctorSpecialties = listOf(
    "Cardiologist", "Dermatologist", "Neurologist", "Pediatrician", "Psychiatrist", "Orthopedic"
)

val mockDoctors = listOf(
    Doctor(
        id = 1,
        name = "Dr. Sarah Ahmed",
        specialty = "Cardiologist",
        experience = 12,
        rating = 4.8,
        consultationFee = 200.0,
        isAvailable = true
    ),
    Doctor(
        id = 2,
        name = "Dr. Ali Hassan",
        specialty = "Dermatologist",
        experience = 8,
        rating = 4.5,
        consultationFee = 150.0,
        isAvailable = false
    ),
    Doctor(
        id = 3,
        name = "Dr. Layla Youssef",
        specialty = "Pediatrician",
        experience = 10,
        rating = 4.9,
        consultationFee = 180.0,
        isAvailable = true
    )
)

val mockUpcomingConsultations = listOf(
    UpcomingConsultation(
        doctorName = "Dr. Sarah Ahmed",
        specialty = "Cardiologist",
        date = "2025-07-10",
        time = "09:30 AM",
        fee = 200.0,
        type = "Video"
    ),
    UpcomingConsultation(
        doctorName = "Dr. Layla Youssef",
        specialty = "Pediatrician",
        date = "2025-07-12",
        time = "04:00 PM",
        fee = 180.0,
        type = "In-Person"
    )
)

val mockConsultationHistory = listOf(
    ConsultationHistory(
        doctorName = "Dr. Ali Hassan",
        specialty = "Dermatologist",
        date = "2025-06-20",
        status = "Completed",
        fee = 150.0,
        diagnosis = "Acne Vulgaris",
        prescription = "Topical retinoids"
    ),
    ConsultationHistory(
        doctorName = "Dr. Sarah Ahmed",
        specialty = "Cardiologist",
        date = "2025-06-10",
        status = "Cancelled",
        fee = 200.0,
        diagnosis = "",
        prescription = ""
    )
)
