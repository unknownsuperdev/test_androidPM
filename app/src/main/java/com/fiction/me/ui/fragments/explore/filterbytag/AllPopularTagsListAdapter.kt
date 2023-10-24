package com.fiction.me.ui.fragments.explore.filterbytag

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.PopularTagsModel
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemPopularTagsForFilterBinding

class AllPopularTagsListAdapter(
    private val onTagClick: (title: String, tagListItemId: String, tagId: Long, isSelected: Boolean) -> Unit
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
        private val onTagClick: (title: String, tagListItemId: String, tagId: Long, isSelected: Boolean) -> Unit
    ) : BaseViewHolder<PopularTagsModel, ViewBinding>(binding) {

        override fun bind(item: PopularTagsModel, context: Context) {
            with(binding) {
                allTagsItem.apply {
                    setTitle(item.title)
                    submitList(item.popularTagsList)
                    itemSelectedClickListener = { tagId, isSelected ->
                        onTagClick(item.title, item.id, tagId, isSelected)
                    }
                }
            }
        }
    }
}