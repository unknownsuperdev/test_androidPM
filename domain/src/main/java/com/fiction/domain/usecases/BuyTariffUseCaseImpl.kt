package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.BuyTariffUseCase
import com.fiction.domain.interactors.GetBookChaptersUseCase
import com.fiction.domain.repository.CoinShopRepo
import com.fiction.entities.response.explore.coinshop.TariffReceiptData
import kotlinx.coroutines.withContext

class BuyTariffUseCaseImpl(
    private val coinShopRepo: CoinShopRepo,
    private val getBookChaptersUseCase: GetBookChaptersUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : BuyTariffUseCase {

    override suspend fun invoke(tariff: String, receiptData: String): ActionResult<Int?> =
        withContext(dispatcher.io) {
            when (val apiData = coinShopRepo.buyTariff(TariffReceiptData(tariff, receiptData))) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        getBookChaptersUseCase(-1L, 0, isPurchased = true)
                        ActionResult.Success(it.balance)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}