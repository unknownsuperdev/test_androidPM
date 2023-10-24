package com.fiction.domain.model.gift

import com.fiction.entities.response.gifts.GiftTypeResponse

enum class GiftType {
    TYPE_WELCOME_BOX,
    TYPE_7_DAYS_CHALLENGE_SUCCESS,
    TYPE_7_DAYS_CHALLENGE_FAIL,
    TYPE_NONE;

    companion object {

        fun from(giftTypeResponse: GiftTypeResponse?) =
            when (giftTypeResponse) {
                GiftTypeResponse.TYPE_WELCOME_BOX -> TYPE_WELCOME_BOX
                GiftTypeResponse.TYPE_7_DAYS_CHALLENGE_SUCCESS -> TYPE_7_DAYS_CHALLENGE_SUCCESS
                GiftTypeResponse.TYPE_7_DAYS_CHALLENGE_FAIL -> TYPE_7_DAYS_CHALLENGE_FAIL
                else -> TYPE_NONE
            }
    }
}
