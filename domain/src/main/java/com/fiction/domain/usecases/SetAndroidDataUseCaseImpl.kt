package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.SetAndroidDataUseCase
import com.fiction.domain.repository.AnalyticRepo
import com.fiction.domain.repository.DataStoreRepository
import com.fiction.domain.utils.Constants
import com.fiction.domain.utils.Constants.Companion.DISABLED_ADVERTISING_ID
import com.fiction.domain.utils.Constants.Companion.NOT_NEED_MAKE_CALL
import com.fiction.entities.request.analytic.GaidRequestBody
import kotlinx.coroutines.flow.first

class SetAndroidDataUseCaseImpl(
    private val analyticRepo: AnalyticRepo,
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : SetAndroidDataUseCase {

    override suspend fun invoke(gaid: String): ActionResult<String> =
        with(dispatcher.io) {
            val gaidLocale = dataStoreRepository.getGaid().first()
            if (gaidLocale == gaid || gaid == DISABLED_ADVERTISING_ID) ActionResult.Success(NOT_NEED_MAKE_CALL)
            else {
                dataStoreRepository.setGaid(gaid)
                when (val apiData = analyticRepo.setAndroidData(GaidRequestBody(gaid))) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let {
                            ActionResult.Success(it.status.toString())
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
            }
        }
}