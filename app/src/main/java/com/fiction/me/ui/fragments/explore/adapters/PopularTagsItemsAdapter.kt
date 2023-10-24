package com.fiction.me.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemPopularTagsBinding
import com.fiction.me.ui.fragments.explore.ExploreFragmentDirections

class PopularTagsItemsAdapter(
    private val onItemNavigateClickListener: (directions: NavDirections) -> Unit,
    private val setOnItemCLick: (tagId: Long, title: String) -> Unit = { _, _ -> },
) : BaseAdapter<ViewBinding, PopularTagsDataModel, BaseViewHolder<PopularTagsDataModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PopularTagsDataModel, ViewBinding> {
        return PopularTagsItemViewHolder(
            ItemPopularTagsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemNavigateClickListener,
            setOnItemCLick
        )
    }

    private class PopularTagsItemViewHolder(
        private val binding: ItemPopularTagsBinding,
        private val onItemClick: (directions: NavDirections) -> Unit,
        private val itemCLick: (tagId: Long, title: String) -> Unit
    ) : BaseViewHolder<PopularTagsDataModel, ViewBinding>(binding) {

        override fun bind(item: PopularTagsDataModel, context: Context) {
            with(binding) {
                title.text = item.title
            }
        }

        override fun onItemClick(item: PopularTagsDataModel) {
            val directions =
                ExploreFragmentDirections.actionExploreFragmentToBooksByTagFragment(item.id)
            onItemClick(directions)
            itemCLick(item.id, item.title)
        }
    }
}