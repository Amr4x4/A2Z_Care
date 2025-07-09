package com.example.a2zcare.data.mapper

import com.example.a2zcare.data.local.entity.EmergencyContactEntity
import com.example.a2zcare.data.model.EmergencyContact2


fun EmergencyContactEntity.toDomain(): EmergencyContact2 {
    return EmergencyContact2(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        relation = relation,
        isPrimary = isPrimary
    )
}

fun EmergencyContact2.toEntity(): EmergencyContactEntity {
    return EmergencyContactEntity(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        relation = relation,
        isPrimary = isPrimary
    )
}
