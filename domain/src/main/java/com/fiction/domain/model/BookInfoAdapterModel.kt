package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.BookItemResponse

data class BookInfoAdapterModel(
    override val id: Long,
    val title: String,
    val imageCover: String,
    val bookInfo: BookInfo,
    val bookData: String = "",
    val views: Int = 0,
    val likes: Int = 0,
    var isSaved: Boolean = false,
    val sale: Int? = null
) : DiffUtilModel<Long>() {

    fun getSale() = if (sale != null) "-$sale%" else null

    companion object {
        fun from(data: BookItemResponse, imgCover: String? = null, imgSummary: String?, authImg: String?): BookInfoAdapterModel =
            with(data) {
                BookInfoAdapterModel(
                    id ?: 0,
                    title ?: "",
                    imgCover ?: covers?.imgCover ?: "",
                    BookInfo.from(data, imgSummary, authImg),
                    description ?: "",
                    views ?: 0,
                    likes ?: 0,
                    isAddedToLibrary ?: false,
                )
            }
    }
}
