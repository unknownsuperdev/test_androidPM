package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.onboarding.BooksForYou

interface GetBooksForYouUseCase {
    suspend operator fun invoke(isClear: Boolean = false): ActionResult<List<BooksForYou>>
}
