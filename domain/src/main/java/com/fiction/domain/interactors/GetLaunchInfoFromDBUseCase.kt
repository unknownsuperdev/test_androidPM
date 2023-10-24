package com.fiction.domain.interactors

interface GetLaunchInfoFromDBUseCase {
    suspend operator fun invoke(): Boolean?
}