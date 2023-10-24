package com.fiction.domain.usecases

import com.fiction.domain.interactors.InsertBookUseCase
import com.fiction.domain.model.bookroom.Book
import com.fiction.domain.repository.BookChapterRepo

class InsertBookUseCaseImpl(
    private val bookChapterRepo: BookChapterRepo
): InsertBookUseCase {
    override suspend fun invoke(book: Book) {
      bookChapterRepo.insertBook(Book.toEntity(book))
    }
}