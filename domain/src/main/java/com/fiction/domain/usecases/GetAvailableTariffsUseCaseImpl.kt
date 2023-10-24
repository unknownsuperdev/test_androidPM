package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetAvailableTariffsUseCase
import com.fiction.domain.model.TariffsItem
import com.fiction.domain.repository.CoinShopRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetAvailableTariffsUseCaseImpl(
    private val coinShopRepo: CoinShopRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : GetAvailableTariffsUseCase {
    private var tariffs: List<TariffsItem> = emptyList()

    override suspend fun invoke(isClearList: Boolean, isMakeCallAnyway: Boolean): ActionResult<List<TariffsItem>> =
        withContext(dispatcher.io) {
            if (isClearList){
                tariffs = emptyList()
                ActionResult.Success(tariffs)
            }
            else if (tariffs.isNotEmpty() && !isMakeCallAnyway) ActionResult.Success(tariffs)
            else
                when (val apiData = coinShopRepo.getAvailableTariffs()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let { response ->
                            tariffs = response.tariffs?.map { TariffsItem.from(it) } ?: emptyList()
                            ActionResult.Success(tariffs)
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }
                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }
}