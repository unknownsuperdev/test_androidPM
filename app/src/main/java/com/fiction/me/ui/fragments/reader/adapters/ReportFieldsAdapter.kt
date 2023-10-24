package com.fiction.me.ui.fragments.reader.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.ReportFieldsModel
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemReportFieldBinding

class ReportFieldsAdapter(
    private val onItemClick: (id: Int, isOther: Boolean) -> Unit
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
        private val onItemClick: (id: Int, isOther: Boolean) -> Unit
    ) : BaseViewHolder<ReportFieldsModel, ViewBinding>(binding) {

        override fun bind(item: ReportFieldsModel, context: Context) {
            binding.reportField.text = item.text
            binding.reportField.isSelected = item.isSelected
        }

        override fun onItemClick(item: ReportFieldsModel) {
            if (item.text == FIELD_TYPE)
                onItemClick(item.id, true)
            else onItemClick(item.id, false)
        }

        companion object {
            private const val FIELD_TYPE = "Other"
        }
    }
}