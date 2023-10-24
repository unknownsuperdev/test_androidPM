package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.BookItemResponse

class AllCurrentReadBooksDataModel(
    override val id: Long,
    val title: String,
    val bookInfo: BookInfo,
    val readProgress: Int = 100,
    val bookData: String = "",
    val imageLink: String = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
    val views: Int = 0,
    val likes: Int = 0
) : DiffUtilModel<Long>() {

    companion object {

        fun from(
            bookResponse: BookItemResponse,
            imgCover: String?,
            imgSummary: String?,
            authImg: String?
        ) =
            with(bookResponse) {
                AllCurrentReadBooksDataModel(
                    id ?: 0,
                    title ?: "",
                    BookInfo.from(bookResponse, imgSummary, authImg),
                    readingProgress?.percent?.toInt() ?: 0,
                    description ?: "",
                    imgCover ?: covers?.imgCover ?: "",
                    views ?: 0,
                    likes ?: 0
                )
            }
    }
}