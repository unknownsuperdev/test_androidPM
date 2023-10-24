package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface BuyTariffUseCase {
    suspend operator fun invoke(tariff: String, receiptData: String): ActionResult<Int?>
}