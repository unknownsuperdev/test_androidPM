package com.fiction.domain.model

import com.fiction.entities.response.reader.ReportParamsRequest

data class ReportParams(
    val percent: Float,
    val reportType: Int,
    val otherText: String? = null
) {


    companion object {
        fun to(reportParams: ReportParams): ReportParamsRequest =
            with(reportParams) {
                ReportParamsRequest(
                    percent,
                    reportType,
                    otherText
                )
            }
    }
}
