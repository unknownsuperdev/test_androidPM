package com.fiction.domain.interactors

import com.fiction.domain.model.BookSettingsData

interface BookSettingsInsertUseCase {
    suspend operator fun invoke(bookSettingsData: BookSettingsData)
}