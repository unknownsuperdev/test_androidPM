package com.name.domain.model.profile

import com.name.entities.responce.profile.SettingResponse

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
