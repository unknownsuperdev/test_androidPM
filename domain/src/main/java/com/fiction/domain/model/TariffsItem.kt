package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.coinshop.TariffResponse

data class TariffsItem(
    override val id: Long,
    val coinsCount: String,
    val price: String,
    val isPaid: Boolean,
    val name: String,
    val item: String,
    val type: String,
    val typeTxt: String,
    val discountPercent: String,
    val coinPicture: String = "",
    val isSelected: Boolean = false,
    val description: String,
    val backGroundType: String,
    val coins: Int
    ) : DiffUtilModel<Long>() {

    companion object {

        fun from(data: TariffResponse): TariffsItem =
            with(data) {
                TariffsItem(
                    id = id ?: 0,
                    type = themeResponse?.lightResponse?.additionalCoinsBackgroundColor ?: "",
                    typeTxt = contentResponse.additionalCoins ?: "",
                    coinsCount = contentResponse.title ?: "",
                    price = price ?: "",
                    discountPercent = contentResponse.discountPercent ?: "",
                    coinPicture = themeResponse?.lightResponse?.iconResponse?.svg ?: "",
                    isPaid = isPaid ?: true,
                    name = name ?: "",
                    description = contentResponse.description ?: "",
                    item = item ?: "",
                    backGroundType = themeResponse?.lightResponse?.backgroundType ?: "default",
                    coins = coins ?: 0
                )
            }
    }
}