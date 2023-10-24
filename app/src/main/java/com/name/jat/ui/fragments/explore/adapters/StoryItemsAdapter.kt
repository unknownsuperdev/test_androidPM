package com.name.jat.ui.fragments.explore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.name.domain.model.StoryDataModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemStoryBinding
import com.name.jat.extensions.dpToPx
import com.name.jat.ui.fragments.explore.ExploreFragmentDirections
import com.bumptech.glide.load.model.LazyHeaders

import com.bumptech.glide.load.model.GlideUrl

class StoryItemsAdapter(
    private val onStoryItemAddedLibraryClick: (parentId: String, storyItemId: Long, isAddedLibrary: Boolean) -> Unit,
    private val onItemClick: (directions: NavDirections) -> Unit
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
        private val onItemClick: (directions: NavDirections) -> Unit
    ) : BaseViewHolder<StoryDataModel, ViewBinding>(binding) {
        @SuppressLint("CheckResult")
        override fun bind(item: StoryDataModel, context: Context) {
            with(binding) {
                if (item.isAddedLibrary)
                    addLibrary.setImageResource(R.drawable.ic_added_library)
                else addLibrary.setImageResource(R.drawable.ic_add_library)

                if(item.picture.isNotEmpty()) {
                    val url = GlideUrl(
                        item.picture, LazyHeaders.Builder()
                            .addHeader("User-Agent", "your-user-agent")
                            .build()
                    )
                    Glide.with(context)
                        .load(url)
                        .transform(RoundedCorners(context.dpToPx(R.dimen.space_4)))
                        .fitCenter()
                        .into(bookPicture)
                }
            }

            binding.addLibrary.setOnClickListener {
                onStoryItemAddedLibraryClick(item.parentId, item.id, !item.isAddedLibrary)
            }
        }

        override fun onItemClick(item: StoryDataModel) {
            val directions =
                ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(item.id)
            onItemClick(directions)
        }
    }
}