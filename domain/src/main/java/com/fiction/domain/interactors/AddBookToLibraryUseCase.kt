package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface AddBookToLibraryUseCase {
    suspend operator fun invoke(bookId: Long): ActionResult<String>
}