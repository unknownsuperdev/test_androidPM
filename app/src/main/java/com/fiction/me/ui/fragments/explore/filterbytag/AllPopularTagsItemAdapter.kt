package com.fiction.me.ui.fragments.explore.filterbytag

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemAllPopularTagsBinding

class AllPopularTagsItemAdapter(
    private val onTagItemClick: (id: Long, isSelected: Boolean) -> Unit
) : BaseAdapter<ViewBinding, PopularTagsDataModel, BaseViewHolder<PopularTagsDataModel, ViewBinding>>() {
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

    private class AllPopularTagsItemViewHolder(
        private val binding: ItemAllPopularTagsBinding,
        private var isSelected: Boolean,
        private val onTagItemClick: (id: Long, isSelected: Boolean) -> Unit
    ) : BaseViewHolder<PopularTagsDataModel, ViewBinding>(binding) {

        override fun bind(item: PopularTagsDataModel, context: Context) {
            with(binding) {
                title.text = item.title

                if (item.isSelected) {
                    selectIcon.setImageResource(R.drawable.ic_selected)
                    container.setBackgroundResource(R.drawable.bg_violet_stroke_purple_r4_field)
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