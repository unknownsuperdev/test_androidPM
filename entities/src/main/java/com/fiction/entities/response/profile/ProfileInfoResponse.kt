package com.fiction.entities.response.profile


import com.google.gson.annotations.SerializedName

data class ProfileInfoResponse(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("balance")
    val balance: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("is_registered")
    val isRegistered: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("setting")
    val setting: SettingResponse?,
    @SerializedName("uuid")
    val uuid: String?
)