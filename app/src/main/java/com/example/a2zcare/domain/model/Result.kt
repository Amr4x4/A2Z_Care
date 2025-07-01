package com.example.a2zcare.domain.model

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : Result<Nothing>()

    data class Loading(
        val isLoading: Boolean = true
    ) : Result<Nothing>()
}