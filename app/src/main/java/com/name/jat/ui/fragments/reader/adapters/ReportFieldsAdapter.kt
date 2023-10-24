package com.name.jat.ui.fragments.reader.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.name.domain.model.ReportFieldsModel
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemReportFieldBinding

class ReportFieldsAdapter(
    private val onItemClick: (isSelected: Boolean, id: Long, isOther: Boolean) -> Unit
) : BaseAdapter<ViewBinding, ReportFieldsModel, BaseViewHolder<ReportFieldsModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ReportFieldsModel, ViewBinding> {
        return ReportFieldsViewHolder(
            ItemReportFieldBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    private class ReportFieldsViewHolder(
        private val binding: ItemReportFieldBinding,
        private val onItemClick: (isSelected: Boolean, id: Long, isOther: Boolean) -> Unit
    ) : BaseViewHolder<ReportFieldsModel, ViewBinding>(binding) {
        private var isSelected = false

        override fun bind(item: ReportFieldsModel, context: Context) {
            binding.reportField.text = item.text
        }

        override fun onItemClick(item: ReportFieldsModel) {
            isSelected = !isSelected
            binding.reportField.isSelected = isSelected
            if (item.text == FIELD_TYPE)
                onItemClick(isSelected, item.id, true)
            else onItemClick(isSelected, item.id, false)
        }

        companion object {
            private const val FIELD_TYPE = "Other"
        }
    }
}