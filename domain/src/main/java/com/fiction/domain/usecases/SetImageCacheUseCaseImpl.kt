package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.SetImageCacheUseCase
import com.fiction.domain.repository.CacheImageRepo
import kotlinx.coroutines.withContext

class SetImageCacheUseCaseImpl(
    private val cacheImageRepo: CacheImageRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : SetImageCacheUseCase {

    override suspend fun invoke(url: String) =
        withContext(dispatcher.io) {
            cacheImageRepo.setCache(url)
            ActionResult.Success(url)
        }

}