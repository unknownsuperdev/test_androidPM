package ru.tripster.domain.usecases.confirm

import ru.tripster.domain.interactors.confirmOrEdit.ChangeItemInAdapterListUseCase
import ru.tripster.domain.model.order.PerTicketOrderDetail

class ChangeItemInAdapterListUseCaseImpl : ChangeItemInAdapterListUseCase {
    private var perTicketOrderDetailList: List<PerTicketOrderDetail> = listOf()

    override fun changeItemInAdapterList(
        perTicketOrderDetail: PerTicketOrderDetail?, list: List<PerTicketOrderDetail>,
        hasCustomPrice: Boolean, isAdded: Boolean, maxPersonCount: Int
    ): List<PerTicketOrderDetail> {
        perTicketOrderDetailList = list

        if (hasCustomPrice) {
            perTicketOrderDetailList = perTicketOrderDetailList.toMutableList().apply {
                this.forEachIndexed { index, item ->
                    this[index] = item.copy(hasCustomPrice = true)
                }
            }
        }

        var totalCount = 0
        var updatedItem = perTicketOrderDetailList.find { it.id == perTicketOrderDetail?.id }
            ?: return perTicketOrderDetailList

        val index = perTicketOrderDetailList.indexOf(updatedItem)

        updatedItem = if (isAdded) {
            updatedItem.copy(count = perTicketOrderDetail?.count?.plus(1) ?: 0)
        } else {
            updatedItem.copy(count = perTicketOrderDetail?.count?.minus(1) ?: 0)
        }

        perTicketOrderDetailList = perTicketOrderDetailList.toMutableList().apply {
            this[index] = updatedItem
        }

        perTicketOrderDetailList.forEach {
            totalCount += it.count
        }

        perTicketOrderDetailList =
            perTicketOrderDetailList.toMutableList().apply {
                this.forEachIndexed { index, perTicketOrderDetail ->
                    this[index] =
                        perTicketOrderDetail.copy(totalCountIsMax = totalCount >= maxPersonCount)
                }
            }

        return perTicketOrderDetailList
    }
}