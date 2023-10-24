package com.fiction.domain.model.bookroom

import com.fiction.entities.roommodels.BooksEntity

data class Book(
    val bookId: Long,
    val lastReadPage: Int,
    val lastReadChapterId: Long
) {

    companion object {

        fun toEntity(book: Book) =
            with(book) {
                BooksEntity(
                    bookId, lastReadPage, lastReadChapterId
                )
            }
    }
}
