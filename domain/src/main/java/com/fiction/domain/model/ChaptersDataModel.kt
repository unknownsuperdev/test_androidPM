package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.ChapterResponse

data class ChaptersDataModel(
    override val id: Long,
    val chapterTitle: String,
    val isFree: Boolean,
    val order: Int,
    val coins: Int,
    val isLastReadChapter: Boolean,
    val isPurchased: Boolean,
) : DiffUtilModel<Long>() {

    companion object {
        fun from(chapterResponse: ChapterResponse, lastReadChapterId: Long?) =
            with(chapterResponse) {
                ChaptersDataModel(
                    id ?: 0,
                    title ?: "",
                    isFree == true,
                    order ?: 0,
                    coins ?: 0,
                    id == lastReadChapterId,
                    isPurchased ?: false
                )
            }
    }
}