package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BalanceResponse
import com.fiction.entities.response.explore.coinshop.AvailableTariffsResponse
import com.fiction.entities.response.explore.coinshop.TariffReceiptData

interface CoinShopRepo {
    suspend fun getBalance(): ActionResult<BaseResultModel<BalanceResponse>>
    suspend fun buyTariff(item: TariffReceiptData): ActionResult<BaseResultModel<BalanceResponse>>
    suspend fun buyChapter(chapterId: Long): ActionResult<BaseResultModel<BalanceResponse>>
    suspend fun getAvailableTariffs(): ActionResult<BaseResultModel<AvailableTariffsResponse>>

    // Await new end-points
//    suspend fun getModal(bookId:Int): ActionResult<BaseResultModel<>>
//    suspend fun getSpecialOffers(chapterId: Int): ActionResult<BaseResultModel<>>
}