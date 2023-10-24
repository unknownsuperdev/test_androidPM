package com.name.domain.interactors

import com.name.core.ActionResult

interface AddBookToLibraryUseCase {
    suspend operator fun invoke(bookId: Long): ActionResult<String>
}