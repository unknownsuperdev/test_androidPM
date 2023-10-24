package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.SendReportUseCase
import com.fiction.domain.model.ReportParams
import com.fiction.domain.repository.ReaderRepo
import kotlinx.coroutines.withContext

class SendReportUseCaseImpl(
    private val readerRepo: ReaderRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : SendReportUseCase {

    override suspend fun invoke(
        chapterId: Long,
        reportParams: ReportParams
    ): ActionResult<String> =
        withContext(dispatcher.io) {
            val reportParamsRequest = ReportParams.to(reportParams)
            when (val apiData = readerRepo.sendReport(chapterId, reportParamsRequest)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        ActionResult.Success(it.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}