package com.fiction.domain.model.onboarding

import com.fiction.entities.response.onboarding.GenderResponse


data class Gender(
    val id: Int,
    val name: String
) {


    companion object {
        fun from(data: GenderResponse): Gender =
            with(data) {
                Gender(
                    id ?: 0,
                    name ?: ""
                )
            }
    }
}
