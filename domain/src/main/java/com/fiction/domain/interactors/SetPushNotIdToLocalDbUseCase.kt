package com.fiction.domain.interactors

interface SetPushNotIdToLocalDbUseCase {
    suspend operator fun invoke(pushId: Int)
}