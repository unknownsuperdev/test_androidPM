package com.fiction.domain.interactors

interface GetImageFromCachingUseCase {
    suspend operator fun invoke(imageUrl: String?): String?
}