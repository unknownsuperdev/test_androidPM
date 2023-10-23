package ru.tripster.domain.usecases

import ru.tripster.core.ActionResult
import ru.tripster.core.CallException
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.Constants.Companion.ERROR_NULL_DATA
import ru.tripster.domain.interactors.TestUseCase
import ru.tripster.domain.model.DataInfo
import ru.tripster.domain.repository.TestRepository
import kotlinx.coroutines.withContext

class TestUseCaseImpl(
    private val testRepository: TestRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : TestUseCase {

    override suspend fun invoke(): ActionResult<List<DataInfo>> = withContext(dispatcher.io) {

        when (val apiData = testRepository.getTestData()) {
            is ActionResult.Success -> {

                apiData.result.data?.memes?.let {
                    ActionResult.Success(it.map { DataInfo.from(it) })
                } ?: ActionResult.Error(CallException(ERROR_NULL_DATA))
            }

            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}