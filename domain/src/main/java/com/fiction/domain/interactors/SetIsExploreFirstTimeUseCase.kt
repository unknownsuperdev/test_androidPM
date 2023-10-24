package com.fiction.domain.interactors

interface SetIsExploreFirstTimeUseCase {

    suspend operator fun invoke(isFirst: Boolean)

}