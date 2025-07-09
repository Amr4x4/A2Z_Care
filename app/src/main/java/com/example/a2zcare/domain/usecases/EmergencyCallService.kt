package com.example.a2zcare.domain.usecases

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager
import androidx.core.content.ContextCompat
import com.example.a2zcare.domain.model.EmergencyConstants
import javax.inject.Inject

class EmergencyCallService @Inject constructor(
    private val context: Context
) {
    fun callAmbulance() {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:${EmergencyConstants.AMBULANCE_NUMBER}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            context.startActivity(intent)
        }
    }

    fun callContact(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            context.startActivity(intent)
        }
    }
}
class SmsService @Inject constructor(
    private val context: Context
) {
    fun sendEmergencySms(phoneNumber: String, message: String) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
