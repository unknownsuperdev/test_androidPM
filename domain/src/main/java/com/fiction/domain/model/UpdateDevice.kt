package com.fiction.domain.model

import com.fiction.entities.response.updatedevice.UpdateDeviceRequest

data class UpdateDevice(
    val appVersion: String,
    val buildVersion: String,
    val udid: String,
) {
    companion object {
        fun to(updateDevice: UpdateDevice, timezone: String): UpdateDeviceRequest =
            with(updateDevice) {
                UpdateDeviceRequest(
                    appVersion = appVersion,
                    buildVersion = buildVersion,
                    udid = udid,
                    timezone = timezone
                )
            }
    }
}
