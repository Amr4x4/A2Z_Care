package com.example.a2zcare.presentation.screens.personal_info_onboarding

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.a2zcare.R
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.selected
import com.example.a2zcare.presentation.theme.unselected
import com.example.a2zcare.presentation.viewmodel.SensorDataViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun SensorDataScreen(
    viewModel: SensorDataViewModel = hiltViewModel(),
    onUploadOrSkip: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedFile by remember { mutableStateOf<File?>(null) }
    var uploadDone by remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    inputStream?.let { stream ->
                        val fileName = getFileName(uri, context) ?: "sensor_data.json"
                        selectedFileName = fileName

                        // Create a temporary file
                        val tempFile = File(context.cacheDir, fileName)
                        val outputStream = FileOutputStream(tempFile)

                        stream.copyTo(outputStream)
                        stream.close()
                        outputStream.close()

                        selectedFile = tempFile
                    }
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    // Error Dialog
    uiState.errorMessage?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearMessages() },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Error",
                        color = Color.White
                    )
                }
            },
            text = {
                Text(
                    text = errorMessage,
                    color = Color.White
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearMessages() }
                ) {
                    Text("OK", color = Color.White)
                }
            },
            containerColor = backgroundColor
        )
    }

    // Success Dialog
    uiState.successMessage?.let { successMessage ->
        AlertDialog(
            onDismissRequest = {
                viewModel.clearMessages()
                if (!uploadDone) {
                    uploadDone = true
                    onUploadOrSkip()
                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.Green
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Success",
                        color = Color.White
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = successMessage,
                        color = Color.White
                    )
                    if (uiState.totalReadings > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total readings imported: ${uiState.totalReadings}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearMessages()
                        if (!uploadDone) {
                            uploadDone = true
                            onUploadOrSkip()
                        }
                    }
                ) {
                    Text("OK", color = Color.White)
                }
            },
            containerColor = backgroundColor
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    onUploadOrSkip()
                },
                enabled = !uiState.isLoading
            ) {
                Text("Skip", color = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Image
            Image(
                painter = painterResource(R.drawable.swart_watch),
                contentDescription = "Smart Watch Icon",
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                modifier = Modifier.size(110.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Title
            Text(
                text = "Sensor Data Upload",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Optionally upload your sensor data file to import readings",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Watch ID Input (optional, just for display/edit)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Watch ID",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.watchId,
                        onValueChange = viewModel::updateWatchId,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Contactless,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        placeholder = { Text("Enter Watch ID", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // File Selection (optional)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "JSON File",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "application/json"
                            }
                            filePickerLauncher.launch(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = selected,
                            contentColor = Color.White,
                            disabledContainerColor = unselected
                        )
                    ) {
                        Text("Select JSON File")
                    }

                    selectedFileName?.let { fileName ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Selected: $fileName",
                            color = Color.Green,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Upload Button (required)
            Button(
                onClick = {
                    selectedFile?.let { file ->
                        viewModel.uploadSensorDataFile(file)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && selectedFile != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = selected,
                    contentColor = Color.White,
                    disabledContainerColor = unselected,
                    disabledContentColor = Color.White.copy(alpha = 0.6f)
                )
            ) {
                if (uiState.isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Uploading...")
                    }
                } else {
                    Text("Upload Sensor Data")
                }
            }
        }

        // Loading Overlay
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Uploading sensor data...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


fun getFileName(uri: Uri, context: android.content.Context): String? {
    return try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex("_display_name")
                if (displayNameIndex != -1) {
                    cursor.getString(displayNameIndex)
                } else null
            } else null
        }
    } catch (e: Exception) {
        null
    }
}

@Preview
@Composable
private fun PreviewSensorData() {
    SensorDataScreen(onUploadOrSkip = {})
}