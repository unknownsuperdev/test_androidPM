package com.fiction.domain.analytic

import com.fiction.entities.request.analytic.AnalyticAppleRequest

data class AnalyticApple(
    val appVersion: String?,
    val authorizationStatus: String?,
    val os: String?,
    val udid: String?,
) {
    companion object {
        fun to(analyticApple: AnalyticApple): AnalyticAppleRequest =
            with(analyticApple) {
                AnalyticAppleRequest(
                    appVersion,
                    authorizationStatus,
                    os,
                    udid
                )
            }
    }
}
