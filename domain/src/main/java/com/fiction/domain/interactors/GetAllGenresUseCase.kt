package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.GenreDataModel

interface GetAllGenresUseCase {
    suspend operator fun invoke(): ActionResult<List<GenreDataModel>>
}