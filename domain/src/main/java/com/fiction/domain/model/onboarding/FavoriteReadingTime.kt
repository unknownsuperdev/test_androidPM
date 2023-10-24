package com.fiction.domain.model.onboarding

import com.fiction.entities.response.onboarding.FavoriteReadingTimeResponse

data class FavoriteReadingTime(
    val id: Int,
    val name: String
) {


    companion object {
        fun from(data: FavoriteReadingTimeResponse): FavoriteReadingTime =
            with(data) {
                FavoriteReadingTime(
                    id ?: 0,
                    name ?: ""
                )
            }
    }
}
