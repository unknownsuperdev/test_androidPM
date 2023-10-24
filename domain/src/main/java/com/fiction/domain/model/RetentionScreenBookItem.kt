package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.GenreResponse

data class RetentionScreenBookItem(
    override val id: Long,
    val image: String,
    val genre: String,
    val title: String,
    val isSelected: Boolean = false,
) : DiffUtilModel<Long>() {

    companion object {
        fun from(data: BookItemResponse, imgCover: String?): RetentionScreenBookItem =
            with(data) {
                RetentionScreenBookItem(
                    id ?: 0,
                    imgCover ?: covers?.imgCover ?: "",
                    getGenres(genres),
                    title ?: ""
                )
            }

        private fun getGenres(genres: List<GenreResponse>?): String {
            return if (genres.isNullOrEmpty()) ""
            else genres.map { it.name }.reduce { result, item -> "$result $item" } ?: ""
        }
    }
}