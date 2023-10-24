package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.GenreResponse
import com.fiction.entities.response.explore.SuggestedBooksItemResponse

data class BestsellersDataModel(
    override val id: Long,
    val bookTitle: String,
    val cover: String,
    val genre: String,
    val bookInfo: BookInfo
) : DiffUtilModel<Long>() {

    companion object {
        fun from(
            data: SuggestedBooksItemResponse,
            imgCover: String?,
            imgSummary: String?,
            authImg: String?
        ): BestsellersDataModel =
            with(data) {
                BestsellersDataModel(
                    id ?: -1,
                    title ?: "",
                    imgCover ?: covers?.imgCover ?: "",
                    getGenres(genres),
                    bookInfo = BookInfo.from(data, imgSummary, authImg)
                )
            }

        private fun getGenres(genres: List<GenreResponse>?): String {
            return if (genres.isNullOrEmpty()) ""
            else genres.map { it.name }.reduce { result, item -> "$result $item" } ?: ""
        }
    }
}
