package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants.Companion.ERROR_NULL_DATA
import com.name.domain.interactors.TestUseCase
import com.name.domain.model.DataInfo
import com.name.domain.repository.TestRepository
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