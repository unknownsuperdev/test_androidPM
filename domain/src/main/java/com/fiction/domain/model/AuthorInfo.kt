package com.fiction.domain.model

import com.fiction.entities.response.explore.AuthorResponse

data class AuthorInfo(
    val id: Long,
    val avatar: String,
    val info: String,
    val penName: String
){

    companion object{
        fun from(authorResponse: AuthorResponse, avatarAuth: String?) =
            with(authorResponse) {
                AuthorInfo(
                    id ?: 0,
                    avatarAuth ?: avatar ?: "",
                    info ?: "",
                    penName ?: ""
                )
            }

        fun emptyItem() =
            AuthorInfo(
                id = 0,
                avatar = "",
                info = "",
                penName = ""
            )
    }
}
