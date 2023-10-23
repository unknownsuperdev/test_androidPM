package ru.tripster.domain.interactors.confirmOrEdit

import ru.tripster.domain.model.order.PerTicketOrderDetail

interface ChangeItemInAdapterListUseCase {
    fun changeItemInAdapterList(
        perTicketOrderDetail: PerTicketOrderDetail? = null,
        list: List<PerTicketOrderDetail>,
        hasCustomPrice: Boolean,
        isAdded: Boolean,
        maxPersonCount: Int
    ): List<PerTicketOrderDetail>
}