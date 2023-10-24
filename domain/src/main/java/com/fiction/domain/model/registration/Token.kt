package com.fiction.domain.model.registration

import com.fiction.entities.response.registration.TokenResponse

data class Token(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {

        fun from(data: TokenResponse): Token =
            with(data) {
                Token(
                    accessToken ?: "",
                    refreshToken ?: ""
                )
            }
    }
}