package com.name.jat.ui.fragments.explore.filterbytag

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.name.domain.model.PopularTagsDataModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemAllPopularTagsBinding

class AllPopularTagsItemAdapter(
    private val onTagItemClick: (id: Long, isSelected: Boolean) -> Unit
) :
    BaseAdapter<ViewBinding, PopularTagsDataModel, BaseViewHolder<PopularTagsDataModel, ViewBinding>>() {
    var isSelected = true
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PopularTagsDataModel, ViewBinding> {
        return AllPopularTagsItemViewHolder(
            ItemAllPopularTagsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            isSelected,
            onTagItemClick
        )
    }

    private  class AllPopularTagsItemViewHolder(
        private val binding: ItemAllPopularTagsBinding,
        private var isSelected: Boolean,
        private val onTagItemClick: (id: Long, isSelected: Boolean) -> Unit
    ) : BaseViewHolder<PopularTagsDataModel, ViewBinding>(binding) {
        @SuppressLint("CheckResult")
        override fun bind(item: PopularTagsDataModel, context: Context) {
            with(binding) {
                title.text = item.title

                if (item.isSelected) {
                    selectIcon.setImageResource(R.drawable.ic_selected)
                    container.setBackgroundResource(R.drawable.bg_all_popular_tags_clicked)
                } else {
                    selectIcon.setImageResource(R.drawable.ic_round_add)
                    container.setBackgroundResource(R.drawable.bg_all_popular_tags)
                }
            }
        }

        override fun onItemClick(item: PopularTagsDataModel) {
            super.onItemClick(item)
                isSelected = !isSelected
                onTagItemClick(item.id, !item.isSelected)
        }

    }
}