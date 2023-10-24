package com.fiction.domain.interactors

interface GetPushNotIdFromLocalDbUseCase {
    suspend operator fun invoke(): Int
}