package com.name.domain.interactors

import com.name.domain.model.BookSettingsData
import kotlinx.coroutines.flow.Flow

interface BookSettingsGetUseCase {
    suspend operator fun invoke() : BookSettingsData
}