package com.example.a2zcare.data.network.response

data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null
)
data class ApiErrorResponse(
    val message: String? = null,
    val error: String? = null,
    val detail: String? = null,
    val title: String? = null,
    val errors: Map<String, List<String>>? = null
) {
    fun getReadableError(): String {
        return when {
            !message.isNullOrBlank() -> message
            !error.isNullOrBlank() -> error
            !detail.isNullOrBlank() -> detail
            !title.isNullOrBlank() -> title
            !errors.isNullOrEmpty() -> {
                errors.values.flatten().firstOrNull() ?: "Validation error"
            }
            else -> "Unknown error occurred"
        }
    }
}