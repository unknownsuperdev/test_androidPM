package com.fiction.domain.interactors

import com.fiction.domain.model.bookroom.Book

interface InsertBookUseCase {
    suspend operator fun invoke(book: Book)
}