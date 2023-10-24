package com.fiction.domain.interactors

interface SavePushTokenLocalUseCase {
    suspend operator fun invoke(pushToken: String)
}