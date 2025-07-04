package com.example.a2zcare.data.remote.response

data class PredictionNested(
    val prediction: String
)

data class HeartDiseaseAIResult(
    val prediction: PredictionNested
)

data class HeartDiseaseAIResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val errors: List<String>,
    val result: HeartDiseaseAIResult?
)
data class HeartDiseaseLatestResult(
    val diseases: String,
    val recordedAt: String
)

data class HeartDiseaseLatestResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val errors: List<String>,
    val result: HeartDiseaseLatestResult?
)

data class HeartDiseasePredictionResponse(
    val prediction: String
)
