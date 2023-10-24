package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface RemoveBookFromLibraryUseCase {
    suspend operator fun invoke(bookId: Long): ActionResult<String>
}