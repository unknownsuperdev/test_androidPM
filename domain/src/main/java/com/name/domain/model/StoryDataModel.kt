package com.name.domain.model

import com.name.core.DiffUtilModel
import com.name.entities.responce.explore.SuggestedBooksItemResponse

data class StoryDataModel(
    override val id: Long,
    val parentId: String,
    val picture: String,
    val isAddedLibrary: Boolean = false
) : DiffUtilModel<Long>() {

    companion object {

        fun from(data: SuggestedBooksItemResponse, parentId: String): StoryDataModel =
            with(data) {
                StoryDataModel(
                    id ?: -1,
                    parentId,
                    cover ?: "",
                    isAddedToLibrary ?: false
                )
            }
    }
}
