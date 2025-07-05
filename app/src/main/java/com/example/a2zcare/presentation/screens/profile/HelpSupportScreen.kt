package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.a2zcare.presentation.common_ui.MiniTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
    navController: NavController
) {
    var selectedFaqIndex by remember { mutableStateOf(-1) }
    val faqItems = listOf(
        FAQItem(
            question = "How do I track my daily steps?",
            answer = "The app automatically tracks your steps using your phone's built-in sensors. Simply carry your phone with you throughout the day. You can view your step count on the home screen and set daily goals in the Activity Tracking section."
        ),
        FAQItem(
            question = "How does the AI blood pressure prediction work?",
            answer = "Our AI model analyzes your activity data, health metrics, and historical patterns to predict your blood pressure levels. The predictions are based on machine learning algorithms trained on health data patterns. Always consult with healthcare professionals for medical decisions."
        ),
        FAQItem(
            question = "How do I set up medication reminders?",
            answer = "Go to the Medication Tracking section, tap 'Add Medication', enter your medication details including name, dosage, and schedule. The app will send you notifications at the specified times and track your medication history."
        ),
        FAQItem(
            question = "What happens during an emergency alert?",
            answer = "When you trigger an emergency alert, the app automatically sends your current location and emergency details to your pre-configured emergency contacts. Make sure to set up your emergency contacts in the settings for this feature to work properly."
        ),
        FAQItem(
            question = "How do I upgrade to VIP membership?",
            answer = "You can upgrade to VIP membership from the Profile screen by tapping on the VIP card. VIP members get access to exclusive features like direct medicine orders, priority doctor consultations, and advanced health insights."
        ),
        FAQItem(
            question = "Why am I not receiving notifications?",
            answer = "Check your notification settings in both the app and your phone's system settings. Make sure notifications are enabled for the HealthCare app. You can also customize notification preferences in the Notifications section of your profile."
        ),
        FAQItem(
            question = "How accurate is the calorie tracking?",
            answer = "Calorie tracking accuracy depends on the completeness and accuracy of the food data you input. The app uses a comprehensive food database to calculate calories. For best results, log all your meals and snacks throughout the day."
        ),
        FAQItem(
            question = "Can I export my health data?",
            answer = "Yes, you can export your health data from the app settings. This includes your activity history, medication records, and health metrics. The exported data can be shared with healthcare providers or used for personal records."
        )
    )

    val supportOptions = listOf(
        SupportOption(
            icon = Icons.Default.Email,
            title = "Email Support",
            description = "Get help via email",
            action = "support@a2zcare.com"
        ),
        SupportOption(
            icon = Icons.Default.Phone,
            title = "Phone Support",
            description = "Call us for immediate assistance",
            action = "+1-800-A2Z-CARE"
        ),
        SupportOption(
            icon = Icons.Default.Chat,
            title = "Live Chat",
            description = "Chat with our support team",
            action = "Available 24/7"
        ),
        SupportOption(
            icon = Icons.Default.Forum,
            title = "Community Forum",
            description = "Connect with other users",
            action = "Join Discussion"
        )
    )

    Scaffold(
        topBar = {
            MiniTopBar(
                title = "Help & Support",
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Contact Support",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(supportOptions) { option ->
                SupportOptionCard(option = option)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Frequently Asked Questions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(faqItems.size) { index ->
                FAQCard(
                    faqItem = faqItems[index],
                    isExpanded = selectedFaqIndex == index,
                    onClick = {
                        selectedFaqIndex = if (selectedFaqIndex == index) -1 else index
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Need More Help?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "If you can't find the answer to your question, don't hesitate to contact our support team. We're here to help you get the most out of your HealthCare app experience.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SupportOptionCard(option: SupportOption) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = option.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = option.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = option.action,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun FAQCard(
    faqItem: FAQItem,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faqItem.question,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = faqItem.answer,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

private data class SupportOption(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val action: String
)

private data class FAQItem(
    val question: String,
    val answer: String
)