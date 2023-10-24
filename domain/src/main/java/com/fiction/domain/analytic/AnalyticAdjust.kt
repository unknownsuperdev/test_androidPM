package com.fiction.domain.analytic

import com.fiction.entities.request.analytic.AnalyticAdjustRequest

data class AnalyticAdjust(
    val adid: String?,
    val bundleId: String?,
    val idfa: String?,
    val udid: String?,
) {
    companion object {
        fun to(analyticAdjust: AnalyticAdjust): AnalyticAdjustRequest =
            with(analyticAdjust) {
                AnalyticAdjustRequest(
                    adid,
                    bundleId,
                    idfa,
                    udid
                )
            }
    }
}
