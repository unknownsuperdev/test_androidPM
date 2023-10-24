package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.GenreResponse
import com.fiction.entities.response.explore.SuggestedBooksItemResponse

data class BooksDataModel(
    override val id: Long,
    val image: String,
    val genre: String,
    val title: String,
    val bookInfo: BookInfo,
    val isAddedLibrary: Boolean = false,
    val sale: Int? = null,
) : DiffUtilModel<Long>() {

    fun getSale() = if (sale != null) "-$sale%" else null

    companion object {

        fun from(data: SuggestedBooksItemResponse, imgCover: String?, imgSummary: String?, authImg: String?): BooksDataModel =
            with(data) {
                BooksDataModel(
                    id ?: -1,
                    imgCover ?: covers?.imgCover ?: "",
                    getGenres(genres),
                    title ?: "",
                    BookInfo.from(data, imgSummary, authImg),
                    isAddedToLibrary ?: false,
                )
            }

        fun from(data: BookItemResponse, imgCover: String?, imgSummary: String?, authImg: String?): BooksDataModel =
            with(data) {
                BooksDataModel(
                    id ?: -1,
                    imgCover ?: covers?.imgCover ?: "",
                    getGenres(genres),
                    title ?: "",
                    BookInfo.from(data, imgSummary, authImg),
                    isAddedToLibrary ?: false,
                )
            }

        private fun getGenres(genres: List<GenreResponse>?): String {
            return if (genres.isNullOrEmpty()) ""
            else genres.map { it.name }.reduce { result, item -> "$result $item" } ?: ""
        }
    }
}