package com.name.jat.ui.fragments.explore.filterbytag

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.name.domain.model.PopularTagsModel
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemPopularTagsForFilterBinding

class AllPopularTagsListAdapter(
    private val onTagClick: (tagListItemId: String, tagId: Long, isSelected: Boolean) -> Unit
) :
    BaseAdapter<ViewBinding, PopularTagsModel, BaseViewHolder<PopularTagsModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PopularTagsModel, ViewBinding> {
        return AllPopularTagsListViewHolder(
            ItemPopularTagsForFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onTagClick
        )
    }

    private class AllPopularTagsListViewHolder(
        private val binding: ItemPopularTagsForFilterBinding,
        private val onTagClick: (tagListItemId: String, tagId: Long, isSelected: Boolean) -> Unit
    ) : BaseViewHolder<PopularTagsModel, ViewBinding>(binding) {

        override fun bind(item: PopularTagsModel, context: Context) {
            with(binding) {
                allTagsItem.apply {
                    setTitle(item.title)
                    submitList(item.popularTagsList)
                    itemSelectedClickListener = { tagId, isSelected ->
                        onTagClick(item.id, tagId, isSelected)
                    }
                }
            }
        }
    }
}