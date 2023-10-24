package com.fiction.domain.usecases

import com.fiction.domain.interactors.BookSettingsInsertUseCase
import com.fiction.domain.model.BookSettingsData
import com.fiction.domain.repository.BookSettingsRepo

class BookSettingsInsertUseCaseImpl(
    private val bookSettingsRepo: BookSettingsRepo
) : BookSettingsInsertUseCase {

    override suspend fun invoke(bookSettingsData: BookSettingsData) {
        bookSettingsRepo.setBookSettingsData(BookSettingsData.toEntity(bookSettingsData))
    }

}