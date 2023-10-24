package com.fiction.domain.model

import com.fiction.entities.response.explore.AuthorResponse
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.GenreResponse

data class BookData(
    val author: AuthorInfo,
    val chapters: List<ChaptersDataModel>,
    val cover: String,
    val description: String,
    val genres: String,
    val id: Long,
    val isAddedToLibrary: Boolean,
    val isCompleted: Boolean,
    val isLiked: Boolean,
    val likes: Int,
    val title: String,
    val views: Int,
    val ageRestriction: Int,
    val isFinished: Boolean,
    val isEsclusive: Boolean
) {

    companion object {
        fun from(bookResponse: BookItemResponse, lastReadChapterId: Long?, imgSummary: String?, avatarAuth: String?) =
            with(bookResponse) {
                BookData(
                    AuthorInfo.from(author ?: AuthorResponse("", 0, "", ""), avatarAuth),
                    chapters?.map { ChaptersDataModel.from(it, lastReadChapterId) } ?: listOf(),
                    imgSummary ?: covers?.imgSummary ?: "",
                    description ?: "",
                    getGenres(genres),
                    id ?: 0,
                    isAddedToLibrary ?: false,
                    isCompleted ?: true,
                    isLiked ?: false,
                    likes ?: 0,
                    title ?: "",
                    views ?: 0,
                    ageRestriction ?: 1,
                    isFinished ?: false,
                    isEsclusive ?: false
                )
            }

        fun emptyItem() = BookData(
            AuthorInfo.emptyItem(),
            chapters = emptyList(),
            cover = "",
            description = "",
            genres = "",
            id = 0,
            isAddedToLibrary = false,
            isCompleted = false,
            isLiked = false,
            likes = 0,
            title = "",
            views = 0,
            ageRestriction = 1,
            isFinished = false,
            isEsclusive = false
        )

        private fun getGenres(genres: List<GenreResponse>?): String {
            return if (genres.isNullOrEmpty()) ""
            else genres.map { it.name }.reduce { result, item -> "$result $item" } ?: ""
        }
    }
}