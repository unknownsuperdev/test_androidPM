package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.SendReaderAnalyticsUseCase
import com.fiction.domain.repository.ReaderRepo
import com.fiction.entities.response.reader.ReaderAnalyticsParamsRequest
import kotlinx.coroutines.withContext

class SendReaderAnalyticsUseCaseImpl(
    private val readerRepo: ReaderRepo,
    private val dispatcher: CoroutineDispatcherProvider,
) : SendReaderAnalyticsUseCase {

    override suspend fun invoke(bookId: Long, sessionId: String, udid: String): ActionResult<Boolean> =
        withContext(dispatcher.io) {
            val paramsRequest = ReaderAnalyticsParamsRequest(bookId, sessionId, udid,)
            when (val apiData = readerRepo.sendReaderAnalytics(paramsRequest)) {
                is ActionResult.Success -> {
                    ActionResult.Success(true)
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}