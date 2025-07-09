package com.example.a2zcare.data.model

import com.example.a2zcare.data.local.entity.Medicine
import com.example.a2zcare.data.local.entity.MedicineSchedule

data class NextMedicineInfo(
    val medicine: Medicine,
    val nextTime: Long,
    val remainingTimeMillis: Long
)

data class MedicineWithSchedules(
    val medicine: Medicine,
    val schedules: List<MedicineSchedule>
)