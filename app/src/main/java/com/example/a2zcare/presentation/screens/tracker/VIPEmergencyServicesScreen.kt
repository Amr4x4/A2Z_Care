package com.example.a2zcare.presentation.screens.tracker

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VIPEmergencyServicesScreen(navController: NavController) {
    var showEmergencyCall by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Services", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE53935)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Emergency Call Button
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE53935))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Emergency,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Emergency Call",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Tap to call emergency services",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showEmergencyCall = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFE53935)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("CALL NOW", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Quick Actions
            item {
                Text(
                    "Quick Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(emergencyQuickActions) { action ->
                        EmergencyActionCard(action = action)
                    }
                }
            }

            // Emergency Contacts
            item {
                Text(
                    "Emergency Contacts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(emergencyContacts) { contact ->
                EmergencyContactCard(contact = contact)
            }

            // Medical Information
            item {
                Text(
                    "Medical Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                MedicalInformationCard()
            }
        }
    }

    if (showEmergencyCall) {
        EmergencyCallDialog(
            onDismiss = { showEmergencyCall = false },
            onConfirm = { showEmergencyCall = false }
        )
    }
}

@Composable
private fun EmergencyActionCard(action: EmergencyAction) {
    Card(
        modifier = Modifier.width(120.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                action.icon,
                contentDescription = null,
                tint = action.color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                action.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmergencyContactCard(contact: EmergencyContact) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE53935).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    contact.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    contact.relation,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    contact.phone,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = { /* Call Contact */ }
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = "Call",
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
private fun MedicalInformationCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Medical Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            MedicalInfoItem("Blood Type", "O+")
            MedicalInfoItem("Allergies", "Penicillin, Shellfish")
            MedicalInfoItem("Medications", "Aspirin (daily)")
            MedicalInfoItem("Conditions", "Hypertension")
            MedicalInfoItem("Emergency Contact", "John")
        }
    }
}
@Composable
private fun MedicalInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun EmergencyCallDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Emergency Call") },
        text = { Text("Are you sure you want to call emergency services?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935)
                )
            ) {
                Text("Call Now", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
data class EmergencyAction(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

data class EmergencyContact(
    val name: String,
    val relation: String,
    val phone: String
)

val emergencyQuickActions = listOf(
    EmergencyAction(
        title = "Call Ambulance",
        icon = Icons.Default.LocalHospital,
        color = Color(0xFFE53935)
    ),
    EmergencyAction(
        title = "Send Location",
        icon = Icons.Default.LocationOn,
        color = Color(0xFF3F51B5)
    ),
    EmergencyAction(
        title = "Share Medical Info",
        icon = Icons.Default.Info,
        color = Color(0xFF009688)
    ),
    EmergencyAction(
        title = "Alert Contacts",
        icon = Icons.Default.Notifications,
        color = Color(0xFFFF9800)
    )
)

val emergencyContacts = listOf(
    EmergencyContact(
        name = "John Doe",
        relation = "Father",
        phone = "+1 555 123 4567"
    ),
    EmergencyContact(
        name = "Jane Smith",
        relation = "Sister",
        phone = "+1 555 987 6543"
    ),
    EmergencyContact(
        name = "Dr. Hassan",
        relation = "Family Doctor",
        phone = "+1 555 112 3344"
    )
)