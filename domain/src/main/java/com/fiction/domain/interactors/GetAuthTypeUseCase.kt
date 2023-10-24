package com.fiction.domain.interactors

import com.fiction.domain.model.enums.AuthType

interface GetAuthTypeUseCase {
    suspend operator fun invoke(): AuthType
}