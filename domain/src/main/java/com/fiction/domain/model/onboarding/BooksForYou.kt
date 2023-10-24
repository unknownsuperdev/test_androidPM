package com.fiction.domain.model.onboarding

import com.fiction.domain.model.BookInfo
import com.fiction.entities.response.explore.GenreResponse

data class BooksForYou(
    val id: Long,
    val cover: String,
    val firstChapter: FirstChapter,
    val genres: String,
    val likes: Int,
    val title: String,
    val views: Int,
    val bookInfo: BookInfo
) {


    companion object {
        fun from(data: BooksForYouResponseItem, imgCover: String?, imgSummary: String?, authImg: String?): BooksForYou =
            with(data) {
                BooksForYou(
                    id ?: 0,
                    imgCover ?: covers?.imgSummary ?: "",
                    firstChapter?.let { FirstChapter.from(it) } ?: FirstChapter.emptyItem(),
                    getGenres(genres),
                    likes ?: 0,
                    title ?: "",
                    views ?: 0,
                    BookInfo.from(data, imgSummary, authImg),
                )
            }

        private fun getGenres(genres: List<GenreResponse>?): String {
            return if (genres.isNullOrEmpty()) ""
            else genres.map { it.name }.reduce { result, item -> "$result $item" } ?: ""
        }
    }
}
