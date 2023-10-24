package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface SetImageCacheUseCase {

    suspend operator fun invoke(url: String): ActionResult<String>

}