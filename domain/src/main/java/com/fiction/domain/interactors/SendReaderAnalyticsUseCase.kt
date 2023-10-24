package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface SendReaderAnalyticsUseCase {
    suspend operator fun invoke(bookId: Long, sessionId: String, udid: String): ActionResult<Boolean>
}