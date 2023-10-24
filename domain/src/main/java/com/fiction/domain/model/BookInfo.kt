package com.fiction.domain.model

import android.os.Parcelable
import com.fiction.domain.model.onboarding.BooksForYouResponseItem
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.GenreResponse
import com.fiction.entities.response.explore.SuggestedBooksItemResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookInfo(
    val cover: String,
    val title: String,
    val genres: String,
    val authorAvatar: String,
    val authorName: String,
    val views: Int,
    val likes: Int,
    val isLike: Boolean,
    val isAddedInLib: Boolean,
    val description: String,
    val chaptersCount: Int,
    val readStatus: Boolean,
    val isFinished: Boolean
) : Parcelable {


    companion object {
        fun from(
            suggestedBooksItemResponse: SuggestedBooksItemResponse,
            imgSummary: String?,
            authImg: String?
        ) =
            with(suggestedBooksItemResponse) {
                BookInfo(
                    imgSummary ?: covers?.imgSummary ?: "",
                    title ?: "",
                    getGenres(genres),
                    authImg ?: author?.avatar ?: "",
                    author?.penName ?: "",
                    views ?: 0,
                    likes ?: 0,
                    isLiked ?: false,
                    isAddedToLibrary ?: false,
                    description ?: "",
                    chapters?.size ?: 0,
                    chapters?.let {
                        if (it.isNotEmpty())
                            it[0].id != readingProgress?.chapterId || readingProgress?.percent != 0.0
                        else false
                    } ?: false,
                    isFinished ?: false
                )
            }

        fun from(bookItemResponse: BookItemResponse, imgSummary: String?, authImg: String?) =
            with(bookItemResponse) {
                BookInfo(
                    imgSummary ?: covers?.imgSummary ?: "",
                    title ?: "",
                    getGenres(genres),
                    authImg ?: author?.avatar ?: "",
                    author?.penName ?: "",
                    views ?: 0,
                    likes ?: 0,
                    isLiked ?: false,
                    isAddedToLibrary ?: false,
                    description ?: "",
                    chapters?.size ?: 0,
                    chapters?.let {
                        if (it.isNotEmpty())
                            it[0].id != readingProgress?.chapterId || readingProgress?.percent != 0.0
                        else false
                    } ?: false,
                    isFinished ?: false
                )
            }

        fun from(bookItemResponse: BooksForYouResponseItem, imgSummary: String?, authImg: String?) =
            with(bookItemResponse) {
                BookInfo(
                    imgSummary ?: covers?.imgSummary ?: "",
                    title ?: "",
                    getGenres(genres),
                    authImg ?: author?.avatar ?: "",
                    author?.penName ?: "",
                    views ?: 0,
                    likes ?: 0,
                    isLiked ?: false,
                    isAddedToLibrary ?: false,
                    description ?: "",
                    chapters?.size ?: 0,
                    chapters?.let { it[0].id != readingProgress?.chapterId || readingProgress?.percent != 0.0 }
                        ?: false,
                    isFinished ?: false
                )
            }

        fun emptyItem() = BookInfo(
            "", "", "", "", "",
            0, 0, false, false, "", 0, false,false
        )


        private fun getGenres(genres: List<GenreResponse>?): String {
            return if (genres.isNullOrEmpty()) ""
            else genres.map { it.name }.reduce { result, item -> "$result $item" } ?: ""
        }
    }
}
