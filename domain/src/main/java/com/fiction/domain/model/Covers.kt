package com.fiction.domain.model

import com.fiction.entities.response.explore.CoversResponse

data class Covers(
    val imgCover: String,
    val imgHorizontal: String,
    val imgSummary: String
) {


    companion object {
        fun from(coversResponse: CoversResponse): Covers =
            with(coversResponse) {
                Covers(
                    imgCover ?: "",
                    imgHorizontal ?: "",
                    imgSummary ?: ""
                )
            }

        fun emptyItem() = Covers("https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612", "", "")

    }
}