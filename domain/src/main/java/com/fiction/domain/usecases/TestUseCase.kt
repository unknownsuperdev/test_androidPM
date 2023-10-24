package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants.Companion.ERROR_NULL_DATA
import com.fiction.domain.interactors.TestUseCase
import com.fiction.domain.model.DataInfo
import com.fiction.domain.repository.TestRepository
import kotlinx.coroutines.withContext

class TestUseCaseImpl(
    private val testRepository: TestRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : TestUseCase {

    override suspend fun invoke(): ActionResult<List<DataInfo>> = withContext(dispatcher.io) {

        when (val apiData = testRepository.getTestData()) {
            is ActionResult.Success -> {

                apiData.result.data?.let {
                    ActionResult.Success(it.map { DataInfo.from(it) })
                } ?: ActionResult.Error(CallException(ERROR_NULL_DATA))
            }

            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}