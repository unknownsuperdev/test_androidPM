package com.fiction.domain.model

import com.fiction.entities.response.explore.BookInfoResponse

data class BookSummaryInfo(
    val bookData: BookData,
    val bookInfo: BookInfo,
    val readingProgress: ReadingProgress
) {

    companion object {
        fun from(bookInfoResponse: BookInfoResponse, imgSummary: String?, avatarAuth: String?) =
            with(bookInfoResponse) {
                BookSummaryInfo(
                    book?.let { BookData.from(it, progress?.chapterId, imgSummary, avatarAuth) }
                        ?: BookData.emptyItem(),
                    book?.let { BookInfo.from(it, imgSummary, avatarAuth) } ?: BookInfo.emptyItem(),
                    progress?.let { ReadingProgress.from(it) } ?: ReadingProgress.emptyItem()
                )
            }
    }
}