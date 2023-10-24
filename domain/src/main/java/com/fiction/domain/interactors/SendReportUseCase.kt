package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.ReportParams

interface SendReportUseCase {
    suspend operator fun invoke(
        chapterId: Long,
        reportParams: ReportParams
    ): ActionResult<String>
}