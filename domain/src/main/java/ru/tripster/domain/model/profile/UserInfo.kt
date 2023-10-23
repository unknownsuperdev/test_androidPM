package ru.tripster.domain.model.profile

import ru.tripster.entities.response.profile.UserInfoResponse

data class UserInfo(
    val avatarImage: String,
    val email: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val expCountPublished: Int
) {
    companion object {
        fun from(userInfoResponse: UserInfoResponse): UserInfo = with(userInfoResponse) {
            UserInfo(
                this.avatarImage ?: "",
                this.email ?: "",
                this.firstName ?: "",
                this.id ?: 0,
                this.lastName ?: "",
                this.expCountPublished ?: 0
            )
        }
    }
}
