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
import com.example.a2zcare.data.model.Doctor
import com.example.a2zcare.data.repository.MockDataRepository
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VIPDoctorConsultationsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Available", "Upcoming", "History")

    // State for doctors
    var doctors by remember { mutableStateOf<List<Doctor>>(emptyList()) }

    // Load doctors from repository
    LaunchedEffect(Unit) {
        val repository = MockDataRepository()
        doctors = repository.getDoctors().first()
    }

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
                0 -> AvailableDoctorsContent(navController, doctors)
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
private fun AvailableDoctorsContent(navController: NavController, doctors: List<Doctor>) {
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
                items(getDoctorSpecialties(doctors)) { specialty ->
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

        items(doctors) { doctor ->
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
        items(getMockUpcomingConsultations()) { consultation ->
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
        items(getMockConsultationHistory()) { consultation ->
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
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            doctor.rating.toString(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Display qualifications
                    if (true) {
                        Text(
                            text = "Qualifications: ${doctor.education}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    // Display hospital
                    if (doctor.hospital.isNotEmpty()) {
                        Text(
                            doctor.hospital,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    if (true) {
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
                "Consultation Fee: Normal",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4CAF50)
            )

            // Display languages
            if (doctor.languages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Languages: ${doctor.languages.joinToString(", ")}",
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
                    onClick = onBookAppointment,
                    modifier = Modifier.weight(1f),
                    enabled = true
                ) {
                    Text("Book Appointment")
                }

                Button(
                    onClick = onConsultNow,
                    modifier = Modifier.weight(1f),
                    enabled = true,
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

// Helper functions and data classes
private fun getDoctorSpecialties(doctors: List<Doctor>): List<String> {
    return listOf("All") + doctors.map { it.specialty }.distinct()
}

// Mock data for consultations (since these aren't in the repository)
data class UpcomingConsultation(
    val id: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val type: String,
    val fee: Double
)

data class ConsultationHistory(
    val id: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val status: String,
    val diagnosis: String,
    val prescription: String,
    val fee: Double
)

private fun getMockUpcomingConsultations(): List<UpcomingConsultation> {
    return listOf(
        UpcomingConsultation(
            "uc1", "Dr. Sarah Johnson", "Cardiologist", "2024-01-20", "10:00 AM", "Video Call", 150.0
        ),
        UpcomingConsultation(
            "uc2", "Dr. Michael Chen", "Dermatologist", "2024-01-22", "2:30 PM", "Phone Call", 120.0
        ),
        UpcomingConsultation(
            "uc3", "Dr. Emily Rodriguez", "Pediatrician", "2024-01-25", "11:00 AM", "Video Call", 100.0
        )
    )
}

private fun getMockConsultationHistory(): List<ConsultationHistory> {
    return listOf(
        ConsultationHistory(
            "ch1", "Dr. David Wilson", "Neurologist", "2024-01-15", "Completed",
            "Migraine headaches", "Prescribed pain medication", 200.0
        ),
        ConsultationHistory(
            "ch2", "Dr. Lisa Thompson", "Psychiatrist", "2024-01-10", "Completed",
            "Anxiety disorder", "Therapy sessions recommended", 180.0
        ),
        ConsultationHistory(
            "ch3", "Dr. Robert Kim", "Orthopedic Surgeon", "2024-01-05", "Cancelled",
            "", "", 250.0
        )
    )
}