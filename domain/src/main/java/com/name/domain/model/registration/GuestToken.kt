package com.name.domain.model.registration

import com.name.entities.responce.registration.GuestTokenResponse

data class GuestToken(
    val accessToken: String
) {
    companion object {

        fun from(data: GuestTokenResponse): GuestToken =
            with(data) {
                GuestToken(
                    accessToken
                )
            }
    }
}