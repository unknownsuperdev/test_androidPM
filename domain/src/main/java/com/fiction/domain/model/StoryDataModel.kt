package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.SuggestedBooksItemResponse

data class StoryDataModel(
    override val id: Long,
    val parentId: String,
    val picture: String,
    val title: String,
    val bookInfo: BookInfo,
    val isAddedLibrary: Boolean = false
) : DiffUtilModel<Long>() {

    companion object {

        fun from(
            data: SuggestedBooksItemResponse,
            parentId: String,
            imgHorizontal: String?,
            imgSummary: String?,
            authorAvatar: String?
        ): StoryDataModel =
            with(data) {
                StoryDataModel(
                    id ?: -1,
                    parentId,
                    imgHorizontal ?: covers?.imgHorizontal ?: "",
                    title ?: "",
                    BookInfo.from(this, imgSummary, authorAvatar),
                    isAddedToLibrary ?: false
                )
            }
    }
}
