package com.fiction.domain.interactors

import com.fiction.domain.model.BooksWithReadProgressBookData

interface GetSearchedMainLibBooksUseCase {
    suspend operator fun invoke(
        word: String
    ): List<BooksWithReadProgressBookData>
}