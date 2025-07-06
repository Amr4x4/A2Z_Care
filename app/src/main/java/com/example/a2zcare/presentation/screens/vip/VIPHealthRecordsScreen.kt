package com.example.a2zcare.presentation.screens.vip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a2zcare.data.repository.MockDataRepository.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VIPHealthRecordsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Medical Records", "Lab Reports", "Prescriptions", "Vaccination")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Records", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Add Record */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Record")
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
            // VIP Health Records Banner
            VIPHealthRecordsBanner()

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, fontSize = 12.sp) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> MedicalRecordsContent()
                1 -> LabReportsContent()
                2 -> PrescriptionsContent()
                3 -> VaccinationContent()
            }
        }
    }
}

@Composable
private fun VIPHealthRecordsBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00BCD4))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.HealthAndSafety,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "VIP Health Records",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Secure • Accessible • Comprehensive",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun MedicalRecordsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mockMedicalRecords) { record ->
            MedicalRecordCard(record = record)
        }
    }
}

@Composable
private fun LabReportsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mockLabReports) { report ->
            LabReportCard(report = report)
        }
    }
}

@Composable
private fun PrescriptionsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mockPrescriptions) { prescription ->
            PrescriptionCard(prescription = prescription)
        }
    }
}

@Composable
private fun VaccinationContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mockVaccinations) { vaccination ->
            VaccinationCard(vaccination = vaccination)
        }
    }
}

@Composable
private fun MedicalRecordCard(record: MedicalRecord) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        record.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        record.doctor,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        record.date,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        record.type,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            if (record.summary.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    record.summary,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
                    onClick = { /* Download */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Download")
                }
            }
        }
    }
}

@Composable
private fun LabReportCard(report:LabReport) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        report.testName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        report.lab,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        report.date,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        Icons.Default.Science,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        report.status,
                        fontSize = 10.sp,
                        color = when (report.status) {
                            "Normal" -> Color(0xFF4CAF50)
                            "Abnormal" -> Color(0xFFE53935)
                            else -> Color.Gray
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (report.results.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Key Results:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                report.results.forEach { result ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            result.parameter,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Text(
                            "${result.value} ${result.unit}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrescriptionCard(prescription: Prescription) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Prescription",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        prescription.doctorName,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        prescription.date,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Icon(
                    Icons.Default.LocalPharmacy,
                    contentDescription = null,
                    tint = Color(0xFF9C27B0),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Medications:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            prescription.medications.forEach { medication ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        medication.name,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        medication.dosage,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* View Full Prescription */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Full")
                }

                Button(
                    onClick = { /* Order Medicines */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Order Medicines")
                }
            }
        }
    }
}

@Composable
private fun VaccinationCard(vaccination: Vaccination) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        vaccination.vaccine,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        vaccination.provider,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        "Administered: ${vaccination.date}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        Icons.Default.Vaccines,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                    if (vaccination.nextDue != null) {
                        Text(
                            "Next: ${vaccination.nextDue}",
                            fontSize = 10.sp,
                            color = Color(0xFFFF9800)
                        )
                    }
                }
            }

            if (vaccination.batchNumber.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Batch: ${vaccination.batchNumber}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
val mockMedicalRecords = listOf(
    MedicalRecord(
        id = "mr1",
        title = "Annual Physical Examination",
        doctor = "Dr. Sarah Johnson",
        date = "2024-01-15",
        type = "General Checkup",
        summary = "All vitals normal. No significant health concerns.",
        attachments = listOf("physical_exam.pdf")
    ),
    MedicalRecord(
        id = "mr2",
        title = "Cardiology Follow-up",
        doctor = "Dr. David Wilson",
        date = "2023-12-20",
        type = "Cardiology",
        summary = "Patient experiencing mild chest pain. ECG recommended.",
        attachments = listOf("ecg_report.pdf", "medication_list.pdf")
    )
)
val mockLabReports = listOf(
    LabReport(
        id = "lr1",
        testName = "Complete Blood Count",
        lab = "City Lab",
        date = "2024-01-12",
        status = "Normal",
        results = listOf(
            LabResult("WBC", "5.8", "×10^9/L", "4.0–11.0", "Normal"),
            LabResult("Hemoglobin", "14.1", "g/dL", "12.0–16.0", "Normal")
        ),
        doctorNotes = "Results within normal range."
    ),
    LabReport(
        id = "lr2",
        testName = "Lipid Profile",
        lab = "Health Diagnostics",
        date = "2024-01-10",
        status = "Abnormal",
        results = listOf(
            LabResult("LDL", "160", "mg/dL", "<100", "High"),
            LabResult("HDL", "42", "mg/dL", ">40", "Normal")
        ),
        doctorNotes = "Dietary changes and medication advised."
    )
)
val mockPrescriptions = listOf(
    Prescription(
        id = "pr1",
        doctorName = "Dr. Lisa Thompson",
        date = "2024-01-05",
        medications = listOf(
            Medication("m1", "Sertraline", "50mg", "Once daily", "30 days"),
            Medication("m2", "Melatonin", "5mg", "Before bedtime", "14 days")
        ),
        notes = "Review in 2 weeks.",
        validUntil = "2024-02-05"
    ),
    Prescription(
        id = "pr2",
        doctorName = "Dr. Emily Rodriguez",
        date = "2023-12-15",
        medications = listOf(
            Medication("m3", "Ibuprofen", "400mg", "3 times daily", "7 days", "Take with meals")
        ),
        notes = "Monitor for side effects.",
        validUntil = "2024-01-15"
    )
)
val mockVaccinations = listOf(
    Vaccination(
        id = "v1",
        vaccine = "COVID-19 Booster",
        provider = "City Hospital",
        date = "2023-11-01",
        batchNumber = "CB2023X01",
        nextDue = "2024-11-01",
        location = "Left Arm",
        notes = "No adverse reaction reported."
    ),
    Vaccination(
        id = "v2",
        vaccine = "Flu Shot",
        provider = "A2Z Clinic",
        date = "2023-10-01",
        batchNumber = "FLU2023B",
        nextDue = "2024-10-01",
        location = "Right Arm",
        notes = "Annual flu vaccine administered."
    )
)

