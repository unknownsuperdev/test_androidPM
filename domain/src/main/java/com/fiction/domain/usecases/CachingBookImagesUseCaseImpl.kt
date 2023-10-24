package com.fiction.domain.usecases

import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants.Companion.IMAGE_SPLIT_WORD
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.repository.ImagesRepo
import com.fiction.entities.roommodels.ImagesEntity
import kotlinx.coroutines.withContext

class CachingBookImagesUseCaseImpl(
    private val imagesRepo: ImagesRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : CachingBookImagesUseCase {

    override suspend fun invoke(imageUrl: List<String?>) {
        withContext(dispatcher.io) {
            imageUrl.forEach {
                val key = it?.substringBefore(IMAGE_SPLIT_WORD)
                val imgSummary = key?.let {
                    imagesRepo.getImage(key)
                }
                if (imgSummary == null) {
                    if (!it.isNullOrEmpty() && !key.isNullOrEmpty())
                        imagesRepo.setImage(ImagesEntity(key, it))
                }
            }
        }
    }
}