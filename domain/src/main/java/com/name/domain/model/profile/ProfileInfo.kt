package com.name.domain.model.profile

import com.name.entities.responce.profile.ProfileInfoResponse

data class ProfileInfo(
    val avatar: String,
    val balance: Int,
    val email: String,
    val isRegistered: Boolean,
    val name: String,
    val setting: Setting,
    val uuid: String
) {


    companion object {
        fun from(data: ProfileInfoResponse): ProfileInfo =
            with(data) {
                ProfileInfo(
                    avatar ?: "",
                    balance ?: 0,
                    email ?: "",
                    isRegistered ?: false,
                    name ?: "",
                    setting?.let { Setting.from(it) } ?: Setting(false, false),
                    uuid ?: ""
                )
            }
    }

}
