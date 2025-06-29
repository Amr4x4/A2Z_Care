package com.example.a2zcare.data.network.response

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("detail")
    val detail: String? = null,
    @SerializedName("instance")
    val instance: String? = null,
    @SerializedName("additionalProp1")
    val additionalProp1: String? = null,
    @SerializedName("additionalProp2")
    val additionalProp2: String? = null,
    @SerializedName("additionalProp3")
    val additionalProp3: String? = null,
    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null
) {
    fun getReadableError(): String {
        return when {
            !detail.isNullOrBlank() -> detail
            !title.isNullOrBlank() -> title
            !errors.isNullOrEmpty() -> {
                errors.values.flatten().firstOrNull() ?: "Validation error"
            }
            else -> "An unknown error occurred"
        }
    }
}