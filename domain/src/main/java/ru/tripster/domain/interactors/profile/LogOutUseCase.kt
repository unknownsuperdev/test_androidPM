package ru.tripster.domain.interactors.profile

interface LogOutUseCase {
    suspend operator fun invoke(): String
}