package com.example.a2zcare.domain.usecases

abstract class UseCase<in P, R> {

    suspend operator fun invoke(parameters: P): R = execute(parameters)

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}