package com.example.a2zcare.presentation.screens.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.data.model.EmergencyContact2
import com.example.a2zcare.presentation.viewmodel.EnhancedHealthDataViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(
    navController: NavController,
    viewModel: EnhancedHealthDataViewModel = hiltViewModel()
) {
    val contacts by viewModel.emergencyContacts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<EmergencyContact2?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Contacts", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Contact")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (contacts.isEmpty()) {
                item {
                    EmptyStateCard(onAddClick = { showAddDialog = true })
                }
            } else {
                items(contacts) { contact ->
                    EmergencyContactCard(
                        contact = contact,
                        onEdit = { editingContact = contact },
                        onDelete = { viewModel.deleteContact(contact.id) },
                        onCall = { viewModel.callContact(contact.phoneNumber) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditContactDialog(
            contact = null,
            onDismiss = { showAddDialog = false },
            onSave = { contact ->
                viewModel.saveContact(contact)
                showAddDialog = false
            }
        )
    }

    editingContact?.let { contact ->
        AddEditContactDialog(
            contact = contact,
            onDismiss = { editingContact = null },
            onSave = { updatedContact ->
                viewModel.updateContact(updatedContact)
                editingContact = null
            }
        )
    }
}

@Composable
private fun EmptyStateCard(onAddClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.ContactPhone,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No Emergency Contacts",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Add contacts who will be notified in case of emergency",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935)
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Contact")
            }
        }
    }
}

@Composable
private fun EmergencyContactCard(
    contact: EmergencyContact2,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCall: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        text = contact.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = contact.relation,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                if (contact.isPrimary) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE53935).copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = "PRIMARY",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = contact.phoneNumber,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            if (contact.email!!.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    contact.email?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCall,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call")
                }

                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE53935)
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
private fun AddEditContactDialog(
    contact: EmergencyContact2?,
    onDismiss: () -> Unit,
    onSave: (EmergencyContact2) -> Unit
) {
    var name by remember { mutableStateOf(contact?.name ?: "") }
    var relation by remember { mutableStateOf(contact?.relation ?: "") }
    var phoneNumber by remember { mutableStateOf(contact?.phoneNumber ?: "") }
    var email by remember { mutableStateOf(contact?.email ?: "") }
    var isPrimary by remember { mutableStateOf(contact?.isPrimary ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (contact == null) "Add Emergency Contact" else "Edit Contact")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = relation,
                    onValueChange = { relation = it },
                    label = { Text("Relation") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email (Optional)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPrimary,
                        onCheckedChange = { isPrimary = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Primary Contact")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phoneNumber.isNotBlank()) {
                        val newContact = EmergencyContact2(
                            id = contact?.id ?: java.util.UUID.randomUUID().toString().hashCode(),
                            name = name,
                            phoneNumber = phoneNumber,
                            email = email,
                            relation = relation,
                            isPrimary = isPrimary
                        )
                        onSave(newContact)
                    }
                },
                enabled = name.isNotBlank() && phoneNumber.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EmergencyCountdownDialog(
    onDismiss: () -> Unit,
    onEmergencyTriggered: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(15) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        onEmergencyTriggered()
    }

    AlertDialog(
        onDismissRequest = { },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "CRITICAL ALERT",
                    color = Color(0xFFE53935),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Your vital signs are critically high!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Color(0xFFE53935).copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timeLeft.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE53935)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Emergency services will be contacted automatically",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("I'M OKAY - CANCEL", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = null
    )
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VIPEmergencyServicesScreen(navController: NavController) {
//    var showEmergencyCall by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Emergency Services", fontWeight = FontWeight.Bold) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFFE53935)
//                )
//            )
//        }
//    ) { padding ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding),
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Emergency Call Button
//            item {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE53935))
//                ) {
//                    Column(
//                        modifier = Modifier.padding(24.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Icon(
//                            Icons.Default.Emergency,
//                            contentDescription = null,
//                            tint = Color.White,
//                            modifier = Modifier.size(48.dp)
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text(
//                            "Emergency Call",
//                            color = Color.White,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Text(
//                            "Tap to call emergency services",
//                            color = Color.White.copy(alpha = 0.8f),
//                            fontSize = 14.sp
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Button(
//                            onClick = { showEmergencyCall = true },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color.White,
//                                contentColor = Color(0xFFE53935)
//                            ),
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Text("CALL NOW", fontWeight = FontWeight.Bold)
//                        }
//                    }
//                }
//            }
//
//            // Quick Actions
//            item {
//                Text(
//                    "Quick Actions",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            item {
//                LazyRow(
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(emergencyQuickActions) { action ->
//                        EmergencyActionCard(action = action)
//                    }
//                }
//            }
//
//            // Emergency Contacts
//            item {
//                Text(
//                    "Emergency Contacts",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            items(emergencyContacts) { contact ->
//                EmergencyContactCard(contact = contact)
//            }
//
//            // Medical Information
//            item {
//                Text(
//                    "Medical Information",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            item {
//                MedicalInformationCard()
//            }
//        }
//    }
//
//    if (showEmergencyCall) {
//        EmergencyCallDialog(
//            onDismiss = { showEmergencyCall = false },
//            onConfirm = { showEmergencyCall = false }
//        )
//    }
//}
//
//@Composable
//private fun EmergencyActionCard(action: EmergencyAction) {
//    Card(
//        modifier = Modifier.width(120.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                action.icon,
//                contentDescription = null,
//                tint = action.color,
//                modifier = Modifier.size(32.dp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                action.title,
//                fontSize = 12.sp,
//                fontWeight = FontWeight.Medium,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//@Composable
//private fun EmergencyContactCard(contact: EmergencyContact) {
//    Card(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .background(Color(0xFFE53935).copy(alpha = 0.1f), CircleShape),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    Icons.Default.Phone,
//                    contentDescription = null,
//                    tint = Color(0xFFE53935),
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    contact.name,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Text(
//                    contact.relation,
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//                Text(
//                    contact.phone,
//                    fontSize = 12.sp,
//                    color = Color.Gray
//                )
//            }
//
//            IconButton(
//                onClick = { /* Call Contact */ }
//            ) {
//                Icon(
//                    Icons.Default.Call,
//                    contentDescription = "Call",
//                    tint = Color(0xFF4CAF50)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun MedicalInformationCard() {
//    Card(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                "Medical Information",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(12.dp))
//
//            MedicalInfoItem("Blood Type", "O+")
//            MedicalInfoItem("Allergies", "Penicillin, Shellfish")
//            MedicalInfoItem("Medications", "Aspirin (daily)")
//            MedicalInfoItem("Conditions", "Hypertension")
//            MedicalInfoItem("Emergency Contact", "John")
//        }
//    }
//}
//@Composable
//private fun MedicalInfoItem(label: String, value: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            label,
//            fontSize = 14.sp,
//            color = Color.Gray
//        )
//        Text(
//            value,
//            fontSize = 14.sp,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//@Composable
//private fun EmergencyCallDialog(
//    onDismiss: () -> Unit,
//    onConfirm: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Emergency Call") },
//        text = { Text("Are you sure you want to call emergency services?") },
//        confirmButton = {
//            Button(
//                onClick = onConfirm,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFFE53935)
//                )
//            ) {
//                Text("Call Now", color = Color.White)
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}
//data class EmergencyAction(
//    val title: String,
//    val icon: ImageVector,
//    val color: Color
//)
//
//data class EmergencyContact(
//    val name: String,
//    val relation: String,
//    val phone: String
//)
//
//val emergencyQuickActions = listOf(
//    EmergencyAction(
//        title = "Call Ambulance",
//        icon = Icons.Default.LocalHospital,
//        color = Color(0xFFE53935)
//    ),
//    EmergencyAction(
//        title = "Send Location",
//        icon = Icons.Default.LocationOn,
//        color = Color(0xFF3F51B5)
//    ),
//    EmergencyAction(
//        title = "Share Medical Info",
//        icon = Icons.Default.Info,
//        color = Color(0xFF009688)
//    ),
//    EmergencyAction(
//        title = "Alert Contacts",
//        icon = Icons.Default.Notifications,
//        color = Color(0xFFFF9800)
//    )
//)
//
//val emergencyContacts = listOf(
//    EmergencyContact(
//        name = "John Doe",
//        relation = "Father",
//        phone = "+1 555 123 4567"
//    ),
//    EmergencyContact(
//        name = "Jane Smith",
//        relation = "Sister",
//        phone = "+1 555 987 6543"
//    ),
//    EmergencyContact(
//        name = "Dr. Hassan",
//        relation = "Family Doctor",
//        phone = "+1 555 112 3344"
//    )
//)