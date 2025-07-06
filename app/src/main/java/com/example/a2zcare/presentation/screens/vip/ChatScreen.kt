package com.example.a2zcare.presentation.screens.vip
/*
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val consultationFee: Double
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

// ==== MOCK DATA ==== //
object MockDataRepository {
    val mockDoctors = listOf(
        Doctor(
            id = "1",
            name = "Dr. Sarah Ahmed",
            specialty = "Cardiologist",
            consultationFee = 100.0
        ),
        Doctor(
            id = "2",
            name = "Dr. Ahmed Ali",
            specialty = "Dermatologist",
            consultationFee = 75.0
        )
    )

    val mockChatMessages = mutableListOf(
        ChatMessage(
            id = "1",
            senderId = "1",
            message = "Hello, how can I help you today?",
            timestamp = "10:00 AM",
            isFromUser = false
        ),
        ChatMessage(
            id = "2",
            senderId = "user",
            message = "I've been feeling dizzy lately.",
            timestamp = "10:01 AM",
            isFromUser = true
        )
    )

    val mockPrescriptionMedicines = listOf(
        PrescriptionMedicine(name = "Paracetamol", dosage = "500mg twice a day"),
        PrescriptionMedicine(name = "Ibuprofen", dosage = "400mg after meals"),
        PrescriptionMedicine(name = "Vitamin D", dosage = "1000 IU daily"),
        PrescriptionMedicine(name = "Aspirin", dosage = "75mg daily"),
        PrescriptionMedicine(name = "Omeprazole", dosage = "20mg before breakfast")
    )

    fun currentFormattedDate(): String {
        return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    doctorId: String
) {
    val doctor = MockDataRepository.mockDoctors.find { it.id == doctorId } ?: return
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(MockDataRepository.mockChatMessages.toList()) }
    var showVideoCall by remember { mutableStateOf(false) }
    var showPrescriptionDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var isCallActive by remember { mutableStateOf(false) }

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
                                doctor.name.take(2).uppercase(),
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
                                        .background(Color(0xFF4CAF50), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Online",
                                    fontSize = 12.sp,
                                    color = Color(0xFF4CAF50)
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
                            timestamp = "Now",
                            isFromUser = true
                        )
                        messages = messages + userMessage
                        val currentMessage = messageText
                        messageText = ""

                        // Simulate doctor response
                        Handler(Looper.getMainLooper()).postDelayed({
                            val doctorResponse = ChatMessage(
                                id = (messages.size + 1).toString(),
                                senderId = doctorId,
                                message = generateDoctorResponse(currentMessage),
                                timestamp = "Now",
                                isFromUser = false
                            )
                            messages = messages + doctorResponse
                        }, 2000)
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
        }
    }

    // Video Call Dialog
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

    // Prescription Dialog
    if (showPrescriptionDialog) {
        PrescriptionDialog(
            doctor = doctor,
            onDismiss = { showPrescriptionDialog = false },
            onSendPrescription = { prescription ->
                val prescriptionMessage = ChatMessage(
                    id = (messages.size + 1).toString(),
                    senderId = doctorId,
                    message = "I've sent you a prescription for: ${prescription.medicines.joinToString(", ") { it.name }}",
                    timestamp = "Now",
                    isFromUser = false,
                    messageType = "prescription",
                    prescription = prescription
                )
                messages = messages + prescriptionMessage
                showPrescriptionDialog = false
            }
        )
    }

    // Report Dialog
    if (showReportDialog) {
        ReportDialog(
            doctor = doctor,
            onDismiss = { showReportDialog = false },
            onSendReport = { report ->
                val reportMessage = ChatMessage(
                    id = (messages.size + 1).toString(),
                    senderId = doctorId,
                    message = "I've prepared a medical report for you: ${report.title}",
                    timestamp = "Now",
                    isFromUser = false,
                    messageType = "report",
                    report = report
                )
                messages = messages + reportMessage
                showReportDialog = false
            }
        )
    }

    // Active Call Overlay
    if (isCallActive) {
        ActiveCallOverlay(
            doctor = doctor,
            onEndCall = { isCallActive = false }
        )
    }
}

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
                // Regular message
                Card(
                    modifier = Modifier.widthIn(max = 280.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (message.isFromUser) Color(0xFF2196F3) else Color(
                            0xFFF5F5F5
                        )
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
                Icon(Icons.Default.Add, contentDescription = "Options")
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
                    "Prescription",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            prescription.medicines.forEach { medicine ->
                Text(
                    "• ${medicine.name} - ${medicine.dosage}",
                    fontSize = 12.sp
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
                    color = Color(0xFF2196F3)
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
                    items(MockDataRepository.mockPrescriptionMedicines) { medicine ->
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
                                Text(medicine.name, fontSize = 14.sp)
                                Text(medicine.dosage, fontSize = 12.sp, color = Color.Gray)
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
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val prescription = Prescription(
                        id = "P${System.currentTimeMillis()}",
                        doctorId = doctor.id,
                        doctorName = doctor.name,
                        medicines = selectedMedicines,
                        instructions = instructions,
                        date = MockDataRepository.currentFormattedDate()
                    )
                    onSendPrescription(prescription)
                },
                enabled = selectedMedicines.isNotEmpty()
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
    var reportSummary by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Medical Report") },
        text = {
            Column {
                OutlinedTextField(
                    value = reportTitle,
                    onValueChange = { reportTitle = it },
                    label = { Text("Report Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reportSummary,
                    onValueChange = { reportSummary = it },
                    label = { Text("Summary") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = diagnosis,
                    onValueChange = { diagnosis = it },
                    label = { Text("Diagnosis") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = recommendations,
                    onValueChange = { recommendations = it },
                    label = { Text("Recommendations") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val report = MedicalReport(
                        id = "R${System.currentTimeMillis()}",
                        title = reportTitle,
                        summary = reportSummary,
                        diagnosis = diagnosis,
                        recommendations = recommendations,
                        doctorId = doctor.id,
                        doctorName = doctor.name,
                        date = MockDataRepository.currentFormattedDate()
                    )
                    onSendReport(report)
                },
                enabled = reportTitle.isNotEmpty() && reportSummary.isNotEmpty()
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
    var isVideoEnabled by remember { mutableStateOf(true) }

    // Simulate call duration
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
        // Video call content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Doctor info and call duration
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color(0xFF2196F3).copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        doctor.name.take(2).uppercase(),
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
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    formatCallDuration(callDuration),
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            // Call controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mute button
                FloatingActionButton(
                    onClick = { isMuted = !isMuted },
                    containerColor = if (isMuted) Color.Red else Color.White.copy(alpha = 0.2f)
                ) {
                    Icon(
                        if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = Color.White
                    )
                }

                // End call button
                FloatingActionButton(
                    onClick = onEndCall,
                    containerColor = Color.Red
                ) {
                    Icon(
                        Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White
                    )
                }

                // Video toggle button
                FloatingActionButton(
                    onClick = { isVideoEnabled = !isVideoEnabled },
                    containerColor = if (isVideoEnabled) Color.White.copy(alpha = 0.2f) else Color.Red
                ) {
                    Icon(
                        if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Video",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

private fun formatCallDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}"
}

private fun generateDoctorResponse(userMessage: String): String {
    val responses = listOf(
        "Thank you for sharing that information. Can you tell me more about your symptoms?",
        "I understand your concern. Based on what you've described, I'd like to ask a few more questions.",
        "That's helpful information. How long have you been experiencing these symptoms?",
        "I see. Are you currently taking any medications?",
        "Thank you for the details. I recommend we schedule a follow-up consultation.",
        "Based on our discussion, I'll prepare a treatment plan for you.",
        "I want to make sure I understand correctly. Can you describe the pain level on a scale of 1-10?",
        "Your symptoms are concerning. I recommend we run some tests to get a better picture."
    )
    return responses.random()
}

 */