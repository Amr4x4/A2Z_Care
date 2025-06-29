package com.example.a2zcare.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDateFormatted(): String {
    val sdf = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
    return sdf.format(Date())
}