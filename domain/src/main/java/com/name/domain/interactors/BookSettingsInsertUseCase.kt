package com.name.domain.interactors

import com.name.domain.model.BookSettingsData

interface BookSettingsInsertUseCase {
    suspend operator fun invoke(bookSettingsData: BookSettingsData)
}