package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.BookItemResponse

data class BooksWithReadProgressBookData(
    override val id: Long,
    val title: String,
    val cover: String,
    val readProgress: Int,
    val bookInfo: BookInfo
) : DiffUtilModel<Long>() {


    companion object {

        fun from(
            data: BookItemResponse,
            imgCover: String?,
            imgSummary: String?,
            authImg: String?
        ): BooksWithReadProgressBookData =
            with(data) {
                BooksWithReadProgressBookData(
                    id ?: 0,
                    title ?: "",
                    imgCover ?: covers?.imgCover ?: "",
                    readingProgress?.percent?.toInt() ?: 0,
                    BookInfo.from(data, imgSummary, authImg)
                )
            }

        fun fromFinish(
            data: BookItemResponse,
            imgCover: String?,
            imgSummary: String?,
            authImg: String?
        ): BooksWithReadProgressBookData =
            with(data) {
                BooksWithReadProgressBookData(
                    id ?: 0,
                    title ?: "",
                    imgCover ?: covers?.imgCover ?: "",
                    100,
                    BookInfo.from(data, imgSummary, authImg)
                )
            }
    }
}
