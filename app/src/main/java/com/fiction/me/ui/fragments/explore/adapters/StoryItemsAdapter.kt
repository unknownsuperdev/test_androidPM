package com.fiction.me.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.StoryDataModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemStoryBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.ui.fragments.explore.ExploreFragmentDirections

class StoryItemsAdapter(
    private val onStoryItemAddedLibraryClick: (parentId: String, storyItemId: Long, isAddedLibrary: Boolean) -> Unit,
    private val onItemClick: (directions: NavDirections, title: String, bookId: Long?) -> Unit
) : BaseAdapter<ViewBinding, StoryDataModel, BaseViewHolder<StoryDataModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<StoryDataModel, ViewBinding> {
        return StoryItemsViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onStoryItemAddedLibraryClick,
            onItemClick
        )
    }

    private class StoryItemsViewHolder(
        private val binding: ItemStoryBinding,
        private val onStoryItemAddedLibraryClick: (parentId: String, storyItemId: Long, isAddedLibrary: Boolean) -> Unit,
        private val onItemClick: (directions: NavDirections, title: String, bookId: Long?) -> Unit
    ) : BaseViewHolder<StoryDataModel, ViewBinding>(binding) {

        override fun bind(item: StoryDataModel, context: Context) {
            with(binding) {
                if (item.isAddedLibrary)
                    addLibrary.setImageResource(R.drawable.ic_added_library)
                else addLibrary.setImageResource(R.drawable.ic_add_library)
                bookPicture.loadUrl(item.picture, context, R.dimen.space_4)
            }

            binding.addLibrary.setOnClickListener {
                onStoryItemAddedLibraryClick(item.parentId, item.id, !item.isAddedLibrary)
            }
        }

        override fun onItemClick(item: StoryDataModel) {
            val directions =
                ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(
                    item.id,
                    bookInfo = item.bookInfo
                )
            onItemClick(directions, item.title, item.id)
        }
    }
}