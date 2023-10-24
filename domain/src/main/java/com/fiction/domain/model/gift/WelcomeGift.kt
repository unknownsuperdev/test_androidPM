package com.fiction.domain.model.gift

import com.fiction.entities.response.gifts.WelcomeGiftResponse

data class WelcomeGift(
    val body: String,
    val createdAt: Long,
    val expiredAt: Long,
    val id: Int,
    val title: String,
    val type: GiftType,
    val coinCount: Int
) {

    companion object {
        fun from(welcomeGiftResponse: WelcomeGiftResponse, content: String, coinCount: Int) =
            with(welcomeGiftResponse) {
                WelcomeGift(
                    content,
                    createdAt ?: 0,
                    expiredAt ?: 0,
                    id ?: 0,
                    title ?: "",
                    GiftType.from(type),
                    coinCount = coinCount
                )
            }
    }
}
