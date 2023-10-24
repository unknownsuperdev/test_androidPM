package com.fiction.domain.model

import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.GenreResponse
import com.fiction.entities.response.explore.SearchBookWithNameResponse

data class SearchBookWithName(
    override val id: Long,
    val bookName: String,
    val contextText: String,
    val imageLink: String,
    val genres: String,
    val bookInfo: BookInfo
) : SearchMainItem(id) {

    companion object {

        fun from(data: BookItemResponse, imgCover: String?, imgSummary: String?, authImg: String?): SearchBookWithName =
            with(data) {
                SearchBookWithName(
                    id ?: 0,
                    title ?: "",
                    description ?: "",
                    imgCover ?: covers?.imgCover ?: "",
                    getGenres(genres),
                    BookInfo.from(data, imgSummary, authImg)
                )
            }

        private fun getGenres(genres: List<GenreResponse>?): String {
            return if (genres.isNullOrEmpty()) ""
            else genres.map { it.name }.reduce { result, item -> "$result $item" } ?: ""
        }
    }
}
