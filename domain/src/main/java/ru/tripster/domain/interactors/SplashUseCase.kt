package ru.tripster.domain.interactors

interface SplashUseCase {
    suspend operator fun invoke(deviceId: String): String
}