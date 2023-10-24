package com.name.domain.usecases

import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.interactors.BookSettingsInsertUseCase
import com.name.domain.model.BookSettingsData
import com.name.domain.repository.BookSettingsRepo

class BookSettingsInsertUseCaseImpl(
    private val bookSettingsRepo: BookSettingsRepo
) : BookSettingsInsertUseCase {

    override suspend fun invoke(bookSettingsData: BookSettingsData) {
        bookSettingsRepo.setBookSettingsData(BookSettingsData.toEntity(bookSettingsData))
    }

}