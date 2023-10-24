package com.fiction.domain.model

import android.os.Parcelable
import com.fiction.entities.response.explore.ReadingProgressResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReadingProgress(
    val chapterId: Long,
    val percent: Double
) : Parcelable {

    companion object {
        fun from(readingProgressResponse: ReadingProgressResponse) =
            with(readingProgressResponse) {
                ReadingProgress(
                    chapterId ?: 0,
                    percent ?: 0.0
                )
            }

        fun emptyItem() =
            ReadingProgress(
                0,
                0.0
            )
    }
}
