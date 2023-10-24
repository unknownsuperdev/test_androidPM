package com.fiction.domain.usecases

import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.BookSettingsGetUseCase
import com.fiction.domain.model.BookSettingsData
import com.fiction.domain.repository.BookSettingsRepo
import kotlinx.coroutines.withContext

class BookSettingsGetUseCaseImpl(
    private val bookSettingsRepo: BookSettingsRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : BookSettingsGetUseCase {

    override suspend fun invoke() = withContext(dispatcher.io) {
        bookSettingsRepo.getBookSettingsData()?.let {
            BookSettingsData.from(it)
        } ?: BookSettingsData.defaultItem()
    }
}