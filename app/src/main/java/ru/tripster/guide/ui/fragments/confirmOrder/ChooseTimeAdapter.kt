package ru.tripster.guide.ui.fragments.confirmOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import ru.tripster.domain.model.order.ChooseTime
import ru.tripster.guide.appbase.adapter.BaseAdapter
import ru.tripster.guide.appbase.adapter.BaseViewHolder
import ru.tripster.guide.databinding.ItemChooseTimeBinding

class ChooseTimeAdapter(private val onTimeClick: ((ChooseTime) -> Unit)) :
    BaseAdapter<ViewBinding, ChooseTime, BaseViewHolder<ChooseTime, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ChooseTime, ViewBinding> {
        return TimeViewHolder(
            ItemChooseTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private inner class TimeViewHolder(private val binding: ItemChooseTimeBinding) :
        BaseViewHolder<ChooseTime, ViewBinding>(binding) {

        override fun onItemClick(item: ChooseTime) {
            onTimeClick(item)
        }

        override fun bind(item: ChooseTime, context: Context) {
            binding.check.isVisible = item.isChecked
            binding.tourHour.text = item.time
        }
    }

}