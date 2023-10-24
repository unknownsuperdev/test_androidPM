package com.fiction.domain.interactors

import com.fiction.domain.model.enums.AuthType

interface SetAuthTypeUseCase {
    suspend operator fun invoke(type: AuthType)
}