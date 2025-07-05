package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a2zcare.presentation.common_ui.MiniTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    navController: NavController
) {
    val privacySections = listOf(
        PrivacySection(
            title = "Information We Collect",
            content = """
                We collect information you provide directly to us, such as:
                
                • Personal Information: Name, email address, phone number, date of birth, gender, and address
                • Health Information: Physical activity data, medication schedules, health goals, weight, height, and blood pressure readings
                • Device Information: Device type, operating system, and app usage data
                • Location Data: GPS location for emergency features (only when enabled)
                • Camera Access: For scanning medication barcodes or QR codes
                
                We collect this information to provide you with personalized health tracking and monitoring services.
            """.trimIndent()
        ),
        PrivacySection(
            title = "How We Use Your Information",
            content = """
                We use the information we collect to:
                
                • Provide and maintain our health tracking services
                • Generate AI-powered health insights and predictions
                • Send medication reminders and health notifications
                • Provide emergency alert services
                • Offer VIP services including doctor consultations and medicine orders
                • Improve our app's functionality and user experience
                • Ensure the security and integrity of our services
                • Comply with legal obligations and protect our rights
                
                Your health data is never sold to third parties for advertising purposes.
            """.trimIndent()
        ),
        PrivacySection(
            title = "Data Security",
            content = """
                We take the security of your health information seriously:
                
                • All data is encrypted in transit and at rest using industry-standard encryption
                • We use secure servers with regular security audits
                • Access to your data is restricted to authorized personnel only
                • We implement multi-factor authentication for all admin accounts
                • Regular security updates and patches are applied
                • We maintain incident response procedures for data breaches
                
                While we strive to protect your information, no method of transmission over the internet is 100% secure.
            """.trimIndent()
        ),
        PrivacySection(
            title = "Data Sharing",
            content = """
                We may share your information in the following circumstances:
                
                • With Healthcare Providers: When you use our VIP consultation services
                • With Emergency Contacts: During emergency alert situations
                • With Service Providers: Third-party vendors who assist us in providing services
                • For Legal Compliance: When required by law or legal process
                • With Your Consent: When you explicitly authorize us to share your information
                
                We do not sell, trade, or rent your personal health information to third parties for commercial purposes.
            """.trimIndent()
        ),
        PrivacySection(
            title = "Your Rights and Choices",
            content = """
                You have the following rights regarding your personal information:
                
                • Access: Request a copy of your personal information
                • Correction: Update or correct inaccurate information
                • Deletion: Request deletion of your personal information
                • Portability: Request your data in a machine-readable format
                • Consent Withdrawal: Withdraw consent for data processing
                • Notification Settings: Control what notifications you receive
                
                To exercise these rights, contact our support team at support@a2zcare.com.
            """.trimIndent()
        ),
        PrivacySection(
            title = "Data Retention",
            content = """
                We retain your information for as long as necessary to provide our services:
                
                • Active Account Data: Retained while your account is active
                • Health Records: Retained for up to 7 years for medical record purposes
                • Emergency Data: Retained for 2 years after last emergency use
                • Usage Analytics: Anonymized data retained for up to 3 years
                • Deleted Account Data: Permanently deleted within 30 days of account deletion
                
                You can delete your account at any time from the Profile settings.
            """.trimIndent()
        ),
        PrivacySection(
            title = "Third-Party Services",
            content = """
                Our app may integrate with third-party services:
                
                • Firebase: For push notifications and analytics
                • Google Play Services: For location services and app updates
                • Payment Processors: For VIP subscription payments
                • Medical Databases: For medication information and drug interactions
                • AI Services: For health predictions and insights
                
                These services have their own privacy policies. We recommend reviewing their policies before using our app.
            """.trimIndent()
        ),
        PrivacySection(
            title = "Children's Privacy",
            content = """
                Our app is not intended for children under 13 years of age:
                
                • We do not knowingly collect personal information from children under 13
                • If we learn that we have collected information from a child under 13, we will delete it immediately
                • Parents or guardians can contact us to request deletion of their child's information
                • Users between 13-18 should have parental consent to use our services
                
                If you believe we have collected information from a child under 13, please contact us immediately.
            """.trimIndent()
        ),
        PrivacySection(
            title = "Updates to Privacy Policy",
            content = """
                We may update this Privacy Policy from time to time:
                
                • We will notify you of any material changes via email or app notification
                • The updated policy will be posted in the app with the effective date
                • Continued use of the app after changes constitutes acceptance of the updated policy
                • You can review the full history of policy changes on our website
                
                We encourage you to review this Privacy Policy periodically to stay informed about our data practices.
            """.trimIndent()
        )
    )

    Scaffold(
        topBar = {
            MiniTopBar(
                title = "Privacy Policy",
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
                            text = "HealthCare App Privacy Policy",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Last Updated: January 2025",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This Privacy Policy explains how HealthCare App collects, uses, and protects your personal and health information when you use our services.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            items(privacySections.size) { index ->
                PrivacySectionCard(section = privacySections[index])
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Contact Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "If you have any questions about this Privacy Policy or our data practices, please contact us at:",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Email: privacy@a2zcare.com\nPhone: +1-800-A2Z-CARE\nAddress: 123 Health St, Wellness City, HC 12345",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrivacySectionCard(section: PrivacySection) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = section.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = section.content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}

private data class PrivacySection(
    val title: String,
    val content: String
)