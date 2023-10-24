package com.fiction.domain.interactors

import com.fiction.domain.model.BookSettingsData

interface BookSettingsGetUseCase {
    suspend operator fun invoke() : BookSettingsData
}