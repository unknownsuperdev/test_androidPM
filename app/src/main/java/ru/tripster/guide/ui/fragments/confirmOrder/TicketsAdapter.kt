package ru.tripster.guide.ui.fragments.confirmOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import ru.tripster.domain.model.events.Status
import ru.tripster.domain.model.order.PerTicketOrderDetail
import ru.tripster.guide.R
import ru.tripster.guide.extensions.dpToPx
import ru.tripster.guide.appbase.adapter.BaseAdapter
import ru.tripster.guide.appbase.adapter.BaseViewHolder
import ru.tripster.guide.databinding.ItemMembersCountBinding
import ru.tripster.guide.extensions.dpToPx
import ru.tripster.guide.extensions.makeVisible
import ru.tripster.guide.extensions.makeVisibleGone
import ru.tripster.guide.extensions.setParticipantAndCurrency

class TicketsAdapter(
    private val onPlusMinusCount: ((PerTicketOrderDetail, Boolean) -> Unit)
) :
    BaseAdapter<ViewBinding, PerTicketOrderDetail, BaseViewHolder<PerTicketOrderDetail, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PerTicketOrderDetail, ViewBinding> {
        return TicketsViewHolder(
            ItemMembersCountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private inner class TicketsViewHolder(private val binding: ItemMembersCountBinding) :
        BaseViewHolder<PerTicketOrderDetail, ViewBinding>(binding) {
        override fun bind(item: PerTicketOrderDetail, context: Context) {
            with(binding) {
                val ticketPrice = item.oneTicketPrice
                type.text = item.title

                if (item.hasCustomPrice || item.oneTicketPrice == 0 || item.pricingModel == context.getString(
                        R.string.per_group
                    )
                ) {
                    price.makeVisibleGone()
                    containerCount.setPadding(0, 0, 0, context.dpToPx(R.dimen.space_12))
                } else {
                    price.makeVisible()
                    containerCount.setPadding(0, 0, 0, context.dpToPx(R.dimen.space_28))
                }

                price.text = context.getString(
                    R.string.price_currency_value
                ).setParticipantAndCurrency(
                    null,
                    ticketPrice, item.currency
                )

                count.text = item.count.toString()
                plusMinusLogic(binding, item)

                plus.setOnClickListener {
                    onPlusMinusCount(item, true)
                    plusMinusLogic(binding, item)
                }

                minus.setOnClickListener {
                    onPlusMinusCount(item, false)
                    plusMinusLogic(binding, item)
                }
            }
        }
    }

    private fun plusMinusLogic(binding: ItemMembersCountBinding, item: PerTicketOrderDetail) {
        with(binding) {
            if (count.text.toString().toInt() == 0 || (item.pricingModel != PricingModel.PER_GROUP.pricingModel && item.status == Status.PAID.value)) {
                minus.isEnabled = false
                minus.alpha = 0.5f
            } else {
                minus.isEnabled = true
                minus.alpha = 1f
            }

            if (item.totalCountIsMax || (item.pricingModel != PricingModel.PER_GROUP.pricingModel && item.status == Status.PAID.value)) {
                plus.isEnabled = false
                plus.alpha = 0.5f
            } else {
                plus.isEnabled = true
                plus.alpha = 1f
            }
        }
    }

}