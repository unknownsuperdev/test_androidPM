package com.fiction.domain.interactors

interface CachingBookImagesUseCase {
    suspend operator fun invoke(imageUrl: List<String?>)
}