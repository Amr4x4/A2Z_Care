package com.example.a2zcare.presentation.screens.vip
/*
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a2zcare.data.model.Doctor

// Sample data - replace with actual data from your repository/viewmodel
private val specialties = listOf(
    "All",
    "Cardiology",
    "Dermatology",
    "Pediatrics",
    "Orthopedics",
    "Neurology",
    "Gynecology",
    "Psychiatry"
)

private val doctors = listOf(
    Doctor(
        id = "1",
        name = "Dr. Sarah Johnson",
        specialty = "Cardiology",
        hospital = "City Medical Center",
        rating = 4.8,
        reviews = 125,
        experience = 15,
        consultationFee = 500,
        originalFee = 800
    ),
    Doctor(
        id = "2",
        name = "Dr. Michael Chen",
        specialty = "Dermatology",
        hospital = "Skin Care Institute",
        rating = 4.9,
        reviews = 98,
        experience = 12,
        consultationFee = 450,
        originalFee = 700
    ),
    Doctor(
        id = "3",
        name = "Dr. Emily Rodriguez",
        specialty = "Pediatrics",
        hospital = "Children's Hospital",
        rating = 4.7,
        reviews = 203,
        experience = 18,
        consultationFee = 400,
        originalFee = 600
    ),
    Doctor(
        id = "4",
        name = "Dr. David Kumar",
        specialty = "Orthopedics",
        hospital = "Bone & Joint Clinic",
        rating = 4.6,
        reviews = 87,
        experience = 20,
        consultationFee = 600,
        originalFee = 900
    )
)

private val availableDates = listOf(
    "Today",
    "Tomorrow",
    "Jul 8",
    "Jul 9",
    "Jul 10"
)

private val availableTimeSlots = listOf(
    "9:00 AM",
    "10:00 AM",
    "11:00 AM",
    "2:00 PM",
    "3:00 PM",
    "4:00 PM",
    "5:00 PM"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VIPDoctorConsultationScreen(navController: NavController) {
    var selectedSpecialty by remember { mutableStateOf("All") }
    var showBookingDialog by remember { mutableStateOf(false) }
    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VIP Consultations", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            // VIP Consultation Benefits
            VIPConsultationBenefitsBanner()

            // Specialty Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(specialties) { specialty ->
                    FilterChip(
                        onClick = { selectedSpecialty = specialty },
                        label = { Text(specialty) },
                        selected = selectedSpecialty == specialty,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // Doctors List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(doctors.filter {
                    selectedSpecialty == "All" || it.specialty == selectedSpecialty
                }) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        onBookConsultation = {
                            selectedDoctor = doctor
                            showBookingDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showBookingDialog && selectedDoctor != null) {
        ConsultationBookingDialog(
            doctor = selectedDoctor!!,
            onDismiss = { showBookingDialog = false },
            onConfirm = {
                showBookingDialog = false
                navController.navigate("consultation_chat/${selectedDoctor!!.id}")
            }
        )
    }
}

@Composable
private fun VIPConsultationBenefitsBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF9C27B0))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.VideoCall,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "VIP Consultation Benefits",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VIPConsultationBenefitItem("⚡ Priority", "Skip waiting")
                VIPConsultationBenefitItem("🎥 HD Video", "Clear quality")
                VIPConsultationBenefitItem("📋 Reports", "Instant access")
            }
        }
    }
}

@Composable
private fun VIPConsultationBenefitItem(title: String, subtitle: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            subtitle,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun DoctorCard(
    doctor: Doctor,
    onBookConsultation: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Doctor Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Color(0xFFE3F2FD),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        doctor.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        doctor.specialty,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        doctor.hospital,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "${doctor.rating} (${doctor.reviews} reviews)",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Experience",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        "${doctor.experience} years",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column {
                    Text(
                        "Consultation Fee",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "₹${doctor.consultationFee}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "₹${doctor.originalFee}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* View Profile */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Profile")
                }

                Button(
                    onClick = onBookConsultation,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Book Now")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConsultationBookingDialog(
    doctor: Doctor,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var selectedTimeSlot by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var consultationType by remember { mutableStateOf("Video Call") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Book Consultation") },
        text = {
            Column {
                Text("Dr. ${doctor.name}")
                Text(doctor.specialty, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                // Consultation Type
                Text("Consultation Type", fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        onClick = { consultationType = "Video Call" },
                        label = { Text("Video Call") },
                        selected = consultationType == "Video Call"
                    )
                    FilterChip(
                        onClick = { consultationType = "Voice Call" },
                        label = { Text("Voice Call") },
                        selected = consultationType == "Voice Call"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date Selection
                Text("Select Date", fontWeight = FontWeight.Medium)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableDates) { date ->
                        FilterChip(
                            onClick = { selectedDate = date },
                            label = { Text(date) },
                            selected = selectedDate == date
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Time Slot Selection
                Text("Select Time", fontWeight = FontWeight.Medium)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableTimeSlots) { time ->
                        FilterChip(
                            onClick = { selectedTimeSlot = time },
                            label = { Text(time) },
                            selected = selectedTimeSlot == time
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = selectedDate.isNotEmpty() && selectedTimeSlot.isNotEmpty()
            ) {
                Text("Book Consultation")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
 */