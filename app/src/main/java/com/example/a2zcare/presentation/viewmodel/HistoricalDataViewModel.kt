package com.example.a2zcare.presentation.viewmodel

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.remote.api.HealthMonitoringApiService
import com.example.a2zcare.data.remote.response.BloodPressureData
import com.example.a2zcare.data.remote.response.HeartDiseaseData
import com.example.a2zcare.data.remote.response.HeartRateData
import com.example.a2zcare.data.remote.response.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoricalDataViewModel @Inject constructor(
    private val apiService: HealthMonitoringApiService,
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _bloodPressureData = mutableStateOf<List<BloodPressureData>>(emptyList())
    val bloodPressureData: State<List<BloodPressureData>> = _bloodPressureData

    private val _heartDiseaseData = mutableStateOf<List<HeartDiseaseData>>(emptyList())
    val heartDiseaseData: State<List<HeartDiseaseData>> = _heartDiseaseData

    private val _heartRateData = mutableStateOf<List<HeartRateData>>(emptyList())
    val heartRateData: State<List<HeartRateData>> = _heartRateData

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _selectedCard = mutableStateOf<String?>(null)
    val selectedCard: State<String?> = _selectedCard

    private val _downloadStatus = mutableStateOf<String?>(null)
    val downloadStatus: State<String?> = _downloadStatus

    fun selectCard(cardType: String) {
        _selectedCard.value = cardType
        loadData(cardType)
    }

    fun clearSelection() {
        _selectedCard.value = null
    }

    private fun loadData(cardType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = tokenManager.getUserId() ?: return@launch
                when (cardType) {
                    "blood_pressure" -> loadBloodPressureData(userId)
                    "heart_disease" -> loadHeartDiseaseData(userId)
                    "heart_rate" -> loadHeartRateData(userId)
                }
            } catch (e: Exception) {
                Log.e("HistoricalDataViewModel", "Error loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadBloodPressureData(userId: String) {
        try {
            val response = apiService.getAllBloodPressurePredictions(userId)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isSuccess) _bloodPressureData.value = it.result.sortedBy { data -> data.timestamp }
                }
            }
        } catch (e: Exception) {
            Log.e("HistoricalDataViewModel", "Error loading blood pressure data", e)
        }
    }

    private suspend fun loadHeartDiseaseData(userId: String) {
        try {
            val response = apiService.getAllHeartDiseasePredictions(userId)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isSuccess) _heartDiseaseData.value = it.result.sortedBy { data -> data.recordedAt }
                }
            }
        } catch (e: Exception) {
            Log.e("HistoricalDataViewModel", "Error loading heart disease data", e)
        }
    }

    private suspend fun loadHeartRateData(userId: String) {
        try {
            val response = apiService.getAllHeartRateCalculations(userId)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isSuccess) _heartRateData.value = it.result.sortedBy { data -> data.recordedAt }
                }
            }
        } catch (e: Exception) {
            Log.e("HistoricalDataViewModel", "Error loading heart rate data", e)
        }
    }

    fun downloadPDF() {
        viewModelScope.launch {
            try {
                _downloadStatus.value = "Generating PDF..."

                val userId = tokenManager.getUserId() ?: return@launch
                if (_bloodPressureData.value.isEmpty()) loadBloodPressureData(userId)
                if (_heartDiseaseData.value.isEmpty()) loadHeartDiseaseData(userId)
                if (_heartRateData.value.isEmpty()) loadHeartRateData(userId)

                generatePDF()
                _downloadStatus.value = "PDF Downloaded Successfully"
            } catch (e: Exception) {
                _downloadStatus.value = "Failed to generate PDF"
                Log.e("HistoricalDataViewModel", "PDF generation failed", e)
            } finally {
                delay(3000)
                _downloadStatus.value = null
            }
        }
    }

    private fun generatePDF() {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas

        val paint = Paint().apply { textSize = 16f; isAntiAlias = true }
        val titlePaint = Paint().apply { textSize = 24f; isFakeBoldText = true; isAntiAlias = true }
        val headerPaint = Paint().apply { textSize = 18f; isFakeBoldText = true; isAntiAlias = true }

        var yPosition = 50f
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())

        fun checkNewPage() {
            if (yPosition > 800f) {
                document.finishPage(page)
                page = document.startPage(pageInfo)
                canvas = page.canvas
                yPosition = 50f
            }
        }

        canvas.drawText("Health Monitoring Report", 50f, yPosition, titlePaint)
        yPosition += 40f
        canvas.drawText("Generated on: $currentDate", 50f, yPosition, paint)
        yPosition += 60f

        canvas.drawText("Blood Pressure History", 50f, yPosition, headerPaint)
        yPosition += 30f
        _bloodPressureData.value.take(10).forEach {
            val timestamp = try {
                dateFormat.format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()).parse(it.timestamp) ?: Date())
            } catch (_: Exception) {
                it.timestamp
            }
            canvas.drawText("$timestamp - SBP: ${it.sbp.toInt()}, DBP: ${it.dbp.toInt()} (${it.category})", 70f, yPosition, paint)
            yPosition += 25f; checkNewPage()
        }

        yPosition += 30f
        canvas.drawText("Heart Disease Predictions", 50f, yPosition, headerPaint)
        yPosition += 30f
        _heartDiseaseData.value.take(10).forEach {
            val timestamp = try {
                dateFormat.format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()).parse(it.recordedAt) ?: Date())
            } catch (_: Exception) {
                it.recordedAt
            }
            canvas.drawText("$timestamp - ${it.diseases}", 70f, yPosition, paint)
            yPosition += 25f; checkNewPage()
        }

        yPosition += 30f
        canvas.drawText("Heart Rate History", 50f, yPosition, headerPaint)
        yPosition += 30f
        _heartRateData.value.take(10).forEach {
            val timestamp = try {
                dateFormat.format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()).parse(it.recordedAt) ?: Date())
            } catch (_: Exception) {
                it.recordedAt
            }
            canvas.drawText("$timestamp - ${it.heartRate} BPM (${it.category})", 70f, yPosition, paint)
            yPosition += 25f; checkNewPage()
        }

        document.finishPage(page)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "health_report_${System.currentTimeMillis()}.pdf")
        try {
            document.writeTo(FileOutputStream(file))
        } catch (e: Exception) {
            Log.e("HistoricalDataViewModel", "Failed to write PDF", e)
        } finally {
            document.close()
        }
    }
}
