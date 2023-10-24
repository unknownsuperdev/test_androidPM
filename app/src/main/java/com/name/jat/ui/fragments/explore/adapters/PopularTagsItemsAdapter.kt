package com.name.jat.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.name.domain.model.PopularTagsDataModel
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemPopularTagsBinding

class PopularTagsItemsAdapter(var onTagClick: (id: Long) -> Unit) :
    BaseAdapter<ViewBinding, PopularTagsDataModel, BaseViewHolder<PopularTagsDataModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PopularTagsDataModel, ViewBinding> {
        return PopularTagsItemViewHolder(
            ItemPopularTagsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onTagClick
        )
    }

    private class PopularTagsItemViewHolder(
        private val binding: ItemPopularTagsBinding,
        private val onTagClick: (id: Long) -> Unit
    ) : BaseViewHolder<PopularTagsDataModel, ViewBinding>(binding) {

        override fun bind(item: PopularTagsDataModel, context: Context) {
            with(binding) {
                title.text = item.title
                tagLayout.setOnClickListener {
                    onTagClick(item.id)
                }
            }
        }
    }
}