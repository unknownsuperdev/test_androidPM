package com.fiction.domain.baseusecase

import androidx.paging.PagingData
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class FlowPagingUseCase<in Params, ReturnType : Any> : KoinComponent {

    private val dispatcher: CoroutineDispatcherProvider by inject()

    protected abstract suspend fun execute(params: Params): Flow<PagingData<ReturnType>>

    suspend operator fun invoke(params: Params): Flow<PagingData<ReturnType>> = execute(params)
        .flowOn(dispatcher.io)
}