package com.example.a2zcare.data.network.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("detail")
    val detail: String? = null,
    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null
) {
    fun getReadableError(): String {
        return errors?.values?.flatten()?.firstOrNull()
            ?: detail
            ?: title
            ?: "An unknown error occurred"
    }
}
