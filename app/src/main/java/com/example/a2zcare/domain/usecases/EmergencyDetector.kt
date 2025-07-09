package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.VitalSigns2
import com.example.a2zcare.domain.model.EmergencyConstants

object EmergencyDetector {
    fun isCriticalCondition(vitals: VitalSigns2): Boolean {
        return vitals.systolicBP >= EmergencyConstants.CRITICAL_SBP ||
                vitals.diastolicBP >= EmergencyConstants.CRITICAL_DBP ||
                vitals.heartRate >= EmergencyConstants.CRITICAL_HR_HIGH ||
                vitals.heartRate <= EmergencyConstants.CRITICAL_HR_LOW
    }

    fun getCriticalReason(vitals: VitalSigns2): String {
        return when {
            vitals.systolicBP >= EmergencyConstants.CRITICAL_SBP ->
                "Critical High Systolic BP: ${vitals.systolicBP} mmHg"
            vitals.diastolicBP >= EmergencyConstants.CRITICAL_DBP ->
                "Critical High Diastolic BP: ${vitals.diastolicBP} mmHg"
            vitals.heartRate >= EmergencyConstants.CRITICAL_HR_HIGH ->
                "Critical High Heart Rate: ${vitals.heartRate} BPM"
            vitals.heartRate <= EmergencyConstants.CRITICAL_HR_LOW ->
                "Critical Low Heart Rate: ${vitals.heartRate} BPM"
            else -> "Unknown Critical Condition"
        }
    }
}
