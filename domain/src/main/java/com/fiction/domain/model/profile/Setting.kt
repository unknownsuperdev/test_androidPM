package com.fiction.domain.model.profile

import com.fiction.entities.response.profile.SettingResponse

data class Setting(
    val autoUnlockPaid: Boolean,
    val readingMode: Boolean
) {


    companion object {
        fun from(setting: SettingResponse): Setting =
            with(setting) {
                Setting(
                    autoUnlockPaid ?: false,
                    readingMode ?: false
                )
            }
    }
}
