package com.fiction.domain.usecases

import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.repository.ImagesRepo
import kotlinx.coroutines.withContext

class GetImageFromCachingUseCaseImpl(
    private val imagesRepo: ImagesRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : GetImageFromCachingUseCase {

    override suspend fun invoke(imageUrl: String?): String? =
        withContext(dispatcher.io) {
            val key =
                imageUrl?.substringBefore(Constants.IMAGE_SPLIT_WORD) ?: return@withContext null
            return@withContext imagesRepo.getImage(key)?.value
        }
}