package com.example.a2zcare.presentation.screens.vip

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a2zcare.data.repository.ChatMessage
import com.example.a2zcare.data.repository.Doctor
import com.example.a2zcare.data.repository.MedicalReport
import com.example.a2zcare.data.repository.MockDataRepository2.currentFormattedDate
import com.example.a2zcare.data.repository.MockDataRepository2.currentFormattedTime
import com.example.a2zcare.data.repository.MockDataRepository2.generateDoctorResponse
import com.example.a2zcare.data.repository.MockDataRepository2.mockChatMessages
import com.example.a2zcare.data.repository.MockDataRepository2.mockDoctors
import com.example.a2zcare.data.repository.MockDataRepository2.mockPrescriptionMedicines
import com.example.a2zcare.data.repository.Prescription
import com.example.a2zcare.data.repository.PrescriptionMedicine
import kotlinx.coroutines.delay
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    doctorId: String = "1"
) {
    val doctor = mockDoctors.find { it.id == doctorId }
        ?: mockDoctors.first()

    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(mockChatMessages.toList()) }
    var showVideoCall by remember { mutableStateOf(false) }
    var showPrescriptionDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var isCallActive by remember { mutableStateOf(false) }
    var isTyping by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF2196F3).copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                doctor.name.split(" ").take(2).joinToString("") { it.first().toString() }.uppercase(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2196F3)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                doctor.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            if (doctor.isAvailable) Color(0xFF4CAF50) else Color(0xFFFF9800),
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    if (doctor.isAvailable) "Online" else "Busy",
                                    fontSize = 12.sp,
                                    color = if (doctor.isAvailable) Color(0xFF4CAF50) else Color(0xFFFF9800)
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showVideoCall = true }) {
                        Icon(Icons.Default.VideoCall, contentDescription = "Video Call")
                    }
                    IconButton(onClick = { isCallActive = true }) {
                        Icon(Icons.Default.Phone, contentDescription = "Voice Call")
                    }
                    IconButton(onClick = { /* More options */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                }
            )
        },
        bottomBar = {
            ChatBottomBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendMessage = {
                    if (messageText.isNotEmpty()) {
                        val userMessage = ChatMessage(
                            id = (messages.size + 1).toString(),
                            senderId = "user",
                            message = messageText,
                            timestamp = currentFormattedTime(),
                            isFromUser = true
                        )
                        messages = messages + userMessage
                        val currentMessage = messageText
                        messageText = ""
                        isTyping = true

                        // Simulate doctor response with delay
                        Handler(Looper.getMainLooper()).postDelayed({
                            isTyping = false
                            val doctorResponse = ChatMessage(
                                id = (messages.size + 1).toString(),
                                senderId = doctorId,
                                message = generateDoctorResponse(currentMessage),
                                timestamp = currentFormattedTime(),
                                isFromUser = false
                            )
                            messages = messages + doctorResponse
                        }, 1500)
                    }
                },
                onAttachFile = { /* Handle file attachment */ },
                onRecordVoice = { /* Handle voice recording */ },
                onShowPrescription = { showPrescriptionDialog = true },
                onShowReport = { showReportDialog = true }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                ChatMessageItem(message = message)
            }

            if (isTyping) {
                item {
                    TypingIndicator()
                }
            }
        }
    }

    // Dialogs and overlays
    if (showVideoCall) {
        VideoCallDialog(
            doctor = doctor,
            onDismiss = { showVideoCall = false },
            onStartCall = {
                showVideoCall = false
                isCallActive = true
            }
        )
    }

    if (showPrescriptionDialog) {
        PrescriptionDialog(
            doctor = doctor,
            onDismiss = { showPrescriptionDialog = false },
            onSendPrescription = { prescription ->
                val prescriptionMessage = ChatMessage(
                    id = (messages.size + 1).toString(),
                    senderId = doctorId,
                    message = "I've sent you a prescription for: ${prescription.medicines.joinToString(", ") { it.name }}",
                    timestamp = currentFormattedTime(),
                    isFromUser = false,
                    messageType = "prescription",
                    prescription = prescription
                )
                messages = messages + prescriptionMessage
                showPrescriptionDialog = false
            }
        )
    }

    if (showReportDialog) {
        ReportDialog(
            doctor = doctor,
            onDismiss = { showReportDialog = false },
            onSendReport = { report ->
                val reportMessage = ChatMessage(
                    id = (messages.size + 1).toString(),
                    senderId = doctorId,
                    message = "I've prepared a medical report for you: ${report.title}",
                    timestamp = currentFormattedTime(),
                    isFromUser = false,
                    messageType = "report",
                    report = report
                )
                messages = messages + reportMessage
                showReportDialog = false
            }
        )
    }

    if (isCallActive) {
        ActiveCallOverlay(
            doctor = doctor,
            onEndCall = { isCallActive = false }
        )
    }
}

// ==== COMPOSABLE COMPONENTS ==== //
@Composable
private fun ChatMessageItem(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
    ) {
        when (message.messageType) {
            "prescription" -> {
                message.prescription?.let { prescription ->
                    PrescriptionMessageCard(prescription = prescription)
                }
            }
            "report" -> {
                message.report?.let { report ->
                    ReportMessageCard(report = report)
                }
            }
            else -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!message.isFromUser) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF2196F3).copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Card(
                        modifier = Modifier.widthIn(max = 280.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (message.isFromUser) Color(0xFF2196F3) else Color(0xFFF5F5F5)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = message.message,
                                color = if (message.isFromUser) Color.White else Color.Black,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = message.timestamp,
                                color = if (message.isFromUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                                fontSize = 10.sp
                            )
                        }
                    }

                    if (message.isFromUser) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF4CAF50), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "You",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF2196F3).copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Doctor is typing",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF2196F3), CircleShape)
                )
            }
        }
    }
}

@Composable
private fun ChatBottomBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onAttachFile: () -> Unit,
    onRecordVoice: () -> Unit,
    onShowPrescription: () -> Unit,
    onShowReport: () -> Unit
) {
    var showOptions by remember { mutableStateOf(false) }

    Column {
        if (showOptions) {
            ChatOptionsBar(
                onAttachFile = onAttachFile,
                onRecordVoice = onRecordVoice,
                onShowPrescription = onShowPrescription,
                onShowReport = onShowReport,
                onDismiss = { showOptions = false }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(
                onClick = { showOptions = !showOptions }
            ) {
                Icon(
                    if (showOptions) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "Options"
                )
            }

            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                maxLines = 3,
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
                onClick = onSendMessage,
                modifier = Modifier.size(48.dp),
                containerColor = Color(0xFF2196F3)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ChatOptionsBar(
    onAttachFile: () -> Unit,
    onRecordVoice: () -> Unit,
    onShowPrescription: () -> Unit,
    onShowReport: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ChatOptionItem(
                    icon = Icons.Default.AttachFile,
                    label = "Attach File",
                    onClick = onAttachFile
                )
            }
            item {
                ChatOptionItem(
                    icon = Icons.Default.Mic,
                    label = "Voice Note",
                    onClick = onRecordVoice
                )
            }
            item {
                ChatOptionItem(
                    icon = Icons.Default.LocalPharmacy,
                    label = "Prescription",
                    onClick = onShowPrescription
                )
            }
            item {
                ChatOptionItem(
                    icon = Icons.Default.Description,
                    label = "Report",
                    onClick = onShowReport
                )
            }
        }
    }
}

@Composable
private fun ChatOptionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF2196F3).copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = Color(0xFF2196F3)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun PrescriptionMessageCard(prescription: Prescription) {
    Card(
        modifier = Modifier.widthIn(max = 300.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocalPharmacy,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Prescription from ${prescription.doctorName}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Medicines:",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )

            prescription.medicines.forEach { medicine ->
                Text(
                    "• ${medicine.name} - ${medicine.dosage}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            if (prescription.instructions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Instructions: ${prescription.instructions}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* View prescription */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("View", fontSize = 12.sp)
                }

                Button(
                    onClick = { /* Download prescription */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    )
                ) {
                    Text("Download", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ReportMessageCard(report: MedicalReport) {
    Card(
        modifier = Modifier.widthIn(max = 300.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Medical Report",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                report.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                report.summary,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* View report */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    )
                ) {
                    Text("View", fontSize = 12.sp)
                }

                Button(
                    onClick = { /* Download report */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Download", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun VideoCallDialog(
    doctor: Doctor,
    onDismiss: () -> Unit,
    onStartCall: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Video Call") },
        text = {
            Column {
                Text("Start a video call with ${doctor.name}?")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Call charges: $${doctor.consultationFee}/hour",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    "Specialty: ${doctor.specialty}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onStartCall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Start Call")
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
private fun PrescriptionDialog(
    doctor: Doctor,
    onDismiss: () -> Unit,
    onSendPrescription: (Prescription) -> Unit
) {
    var selectedMedicines by remember { mutableStateOf(listOf<PrescriptionMedicine>()) }
    var instructions by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Prescription") },
        text = {
            Column {
                Text("Add medicines to prescription:")
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(mockPrescriptionMedicines) { medicine ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedMedicines = if (selectedMedicines.contains(medicine)) {
                                        selectedMedicines - medicine
                                    } else {
                                        selectedMedicines + medicine
                                    }
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedMedicines.contains(medicine),
                                onCheckedChange = { checked ->
                                    selectedMedicines = if (checked) {
                                        selectedMedicines + medicine
                                    } else {
                                        selectedMedicines - medicine
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    medicine.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    medicine.dosage,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    placeholder = { Text("Additional instructions for the patient...") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedMedicines.isNotEmpty()) {
                        val prescription = Prescription(
                            id = UUID.randomUUID().toString(),
                            doctorId = doctor.id,
                            doctorName = doctor.name,
                            medicines = selectedMedicines,
                            instructions = instructions,
                            date = currentFormattedDate()
                        )
                        onSendPrescription(prescription)
                    }
                },
                enabled = selectedMedicines.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Send Prescription")
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
private fun ReportDialog(
    doctor: Doctor,
    onDismiss: () -> Unit,
    onSendReport: (MedicalReport) -> Unit
) {
    var reportTitle by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Medical Report") },
        text = {
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = reportTitle,
                        onValueChange = { reportTitle = it },
                        label = { Text("Report Title") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Consultation Report") }
                    )
                }

                item {
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = { Text("Summary") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        placeholder = { Text("Brief summary of the consultation...") }
                    )
                }

                item {
                    OutlinedTextField(
                        value = diagnosis,
                        onValueChange = { diagnosis = it },
                        label = { Text("Diagnosis") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        placeholder = { Text("Primary diagnosis and findings...") }
                    )
                }

                item {
                    OutlinedTextField(
                        value = recommendations,
                        onValueChange = { recommendations = it },
                        label = { Text("Recommendations") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        placeholder = { Text("Treatment recommendations and follow-up...") }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (reportTitle.isNotEmpty() && diagnosis.isNotEmpty()) {
                        val report = MedicalReport(
                            id = UUID.randomUUID().toString(),
                            title = reportTitle,
                            summary = summary,
                            diagnosis = diagnosis,
                            recommendations = recommendations,
                            doctorId = doctor.id,
                            doctorName = doctor.name,
                            date = currentFormattedDate()
                        )
                        onSendReport(report)
                    }
                },
                enabled = reportTitle.isNotEmpty() && diagnosis.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Text("Send Report")
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
private fun ActiveCallOverlay(
    doctor: Doctor,
    onEndCall: () -> Unit
) {
    var callDuration by remember { mutableStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }
    var isVideoEnabled by remember { mutableStateOf(false) }

    // Simulate call duration timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            callDuration++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Doctor info
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFF2196F3).copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    doctor.name.split(" ").take(2).joinToString("") { it.first().toString() }.uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                doctor.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                doctor.specialty,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Call duration
            Text(
                formatCallDuration(callDuration),
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Call controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CallControlButton(
                    icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    backgroundColor = if (isMuted) Color.Red else Color.White.copy(alpha = 0.2f),
                    onClick = { isMuted = !isMuted }
                )

                CallControlButton(
                    icon = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                    backgroundColor = if (isSpeakerOn) Color(0xFF2196F3) else Color.White.copy(alpha = 0.2f),
                    onClick = { isSpeakerOn = !isSpeakerOn }
                )

                CallControlButton(
                    icon = if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                    backgroundColor = if (isVideoEnabled) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.2f),
                    onClick = { isVideoEnabled = !isVideoEnabled }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // End call button
            FloatingActionButton(
                onClick = onEndCall,
                modifier = Modifier.size(64.dp),
                containerColor = Color.Red
            ) {
                Icon(
                    Icons.Default.CallEnd,
                    contentDescription = "End Call",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun CallControlButton(
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        containerColor = backgroundColor
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun formatCallDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}
