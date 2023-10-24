package com.fiction.domain.model.onboarding

import com.fiction.entities.response.onboarding.FirstChapterResponse

data class FirstChapter(
    val id: Long,
    val text: String,
    val title: String
) {


    companion object {
        fun from(data: FirstChapterResponse): FirstChapter =
            with(data) {
                FirstChapter(
                    id ?: 0,
                    text ?: "",
                    title ?: ""
                )
            }

        fun emptyItem() = FirstChapter(
            id = 0,
            text = "",
            title = ""
        )
    }
}
