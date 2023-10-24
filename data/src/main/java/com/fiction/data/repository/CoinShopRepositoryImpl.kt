package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.CoinShopService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.CoinShopRepo
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BalanceResponse
import com.fiction.entities.response.explore.coinshop.AvailableTariffsResponse
import com.fiction.entities.response.explore.coinshop.TariffReceiptData

class CoinShopRepositoryImpl(private val coinShopService: CoinShopService) : CoinShopRepo {
    override suspend fun getBalance(): ActionResult<BaseResultModel<BalanceResponse>> =
        makeApiCall {
            analyzeResponse(
                coinShopService.getBalance()
            )
        }

    override suspend fun buyTariff(item: TariffReceiptData): ActionResult<BaseResultModel<BalanceResponse>> =
        makeApiCall {
            analyzeResponse(
                coinShopService.buyTariff(item)
            )
        }

    override suspend fun buyChapter(chapterId: Long): ActionResult<BaseResultModel<BalanceResponse>> =
        makeApiCall {
            analyzeResponse(
                coinShopService.buyChapter(chapterId)
            )
        }

    override suspend fun getAvailableTariffs(): ActionResult<BaseResultModel<AvailableTariffsResponse>> =
        makeApiCall {
            analyzeResponse(
                coinShopService.getAvailableTariffs()
            )
        }
}