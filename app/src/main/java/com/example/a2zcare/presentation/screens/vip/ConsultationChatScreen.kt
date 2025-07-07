package com.example.a2zcare.presentation.screens.vip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.a2zcare.data.repository.MockDataRepository2.mockDoctors
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationChatScreen(
    navController: NavController,
    doctorId: String = "1"
) {
    val doctor = mockDoctors.find { it.id.toString() == doctorId }
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(mockChatMessages) }
    var isVideoCall by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFE3F2FD), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(doctor?.name ?: "Doctor", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Online", fontSize = 12.sp, color = Color(0xFF4CAF50))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isVideoCall = true }) {
                        Icon(Icons.Default.VideoCall, contentDescription = "Video Call")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Call, contentDescription = "Voice Call")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    ChatMessageItem(message = message)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type your message...") },
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    if (messageText.isNotEmpty()) {
                        messages = messages + ChatMessage(
                            id = (messages.size + 1).toString(),
                            text = messageText,
                            isFromUser = true,
                            timestamp = System.currentTimeMillis()
                        )
                        messageText = ""
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    }

    if (isVideoCall) {
        VideoCallScreen(doctor = doctor as Doctor?, onEndCall = { isVideoCall = false })
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            Box(
                modifier = Modifier.size(32.dp).background(Color(0xFFE3F2FD), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            colors = CardDefaults.cardColors(containerColor = if (message.isFromUser) Color(0xFF1976D2) else Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(message.text, color = if (message.isFromUser) Color.White else Color.Black, fontSize = 14.sp)
                Text(
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
                    color = if (message.isFromUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        if (message.isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.size(32.dp).background(Color(0xFF4CAF50), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("You", color = Color.White, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun VideoCallScreen(
    doctor: Doctor?,
    onEndCall: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f).background(Color(0xFF1A1A1A)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(120.dp).background(Color(0xFF2196F3), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(60.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(doctor?.name ?: "Doctor", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Connected", color = Color(0xFF4CAF50), fontSize = 16.sp)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(56.dp).background(Color(0xFF333333), CircleShape)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Mute", tint = Color.White)
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(56.dp).background(Color(0xFF333333), CircleShape)
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = "Camera", tint = Color.White)
                }
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier.size(56.dp).background(Color(0xFFE53935), CircleShape)
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
                }
            }
        }
    }
}

// Required models
data class ChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long
)

val mockChatMessages = listOf(
    ChatMessage("1", "Hello Doctor", true, System.currentTimeMillis() - 60000),
    ChatMessage("2", "Hi, how can I help you?", false, System.currentTimeMillis() - 50000),
    ChatMessage("3", "I have a headache since morning.", true, System.currentTimeMillis() - 40000),
    ChatMessage("4", "Do you also have fever?", false, System.currentTimeMillis() - 30000)
)