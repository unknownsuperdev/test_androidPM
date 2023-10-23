package ru.tripster.guide.ui.fragments.order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import ru.tripster.domain.model.DataChip
import ru.tripster.guide.appbase.adapter.BaseAdapter
import ru.tripster.guide.appbase.adapter.BaseViewHolder
import ru.tripster.guide.databinding.ItemChipBinding

class ChipsAdapter : BaseAdapter<ViewBinding, DataChip, BaseViewHolder<DataChip, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<DataChip, ViewBinding> {
        return  ViewHolderChip(
            ItemChipBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolderChip(private val binding: ItemChipBinding) :
        BaseViewHolder<DataChip, ViewBinding>(binding) {
        override fun bind(item: DataChip, context: Context) {
            binding.title.text = item.name
            binding.count.text = item.count.toString()
        }
    }
}