package com.fiction.domain.model

import com.fiction.entities.response.explore.ChapterInfoResponse
import com.fiction.entities.roommodels.ChapterEntity

data class ChapterInfo(
    val id: Long,
    val text: String,
    val title: String,
    val order: Int,
    val coins: Int,
    val isPurchased: Boolean,
    val bookId: Long? = null
) {

    companion object {
        fun from(data: ChapterInfoResponse, bookId: Long?) =
            with(data) {
                ChapterInfo(
                    id ?: 0,
                    text ?: "",
                    title ?: "",
                    order ?: 0,
                    coins ?: 0,
                    isPurchased ?: false,
                    bookId
                )
            }

        fun toEntity(chapter: ChapterInfo) =
            with(chapter) {
                ChapterEntity(
                    id ?: 0,
                    title ?: "",
                    text ?: "",
                    order ?: 0,
                    bookId ?: 0
                )
            }

        fun toData(chapterEntity: ChapterEntity) =
            with(chapterEntity) {
                ChapterInfo(
                    chapterId ?: 0,
                    text ?: "",
                    title ?: "",
                    order ?: 0,
                    0,
                    true,
                    bookId ?: 0
                )
            }
    }
}