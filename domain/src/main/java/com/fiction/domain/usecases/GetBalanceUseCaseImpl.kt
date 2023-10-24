package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetBalanceUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.repository.CoinShopRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetBalanceUseCaseImpl(
    private val coinShopRepo: CoinShopRepo,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetBalanceUseCase {

    override suspend fun invoke(): ActionResult<Int?> =
        withContext(dispatcher.io) {
            when (val apiData = coinShopRepo.getBalance()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        response.balance?.let { balance ->
                            getProfileInfoUseCase.updateBalance(
                                balance
                            )
                        }
                        ActionResult.Success(response.balance)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}