package com.name.jat.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.name.domain.model.BooksDataModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemSuggestedBooksBinding
import com.name.jat.extensions.dpToPx
import com.name.jat.ui.fragments.explore.ExploreFragmentDirections

class SuggestedBooksItemsAdapter(
    private val onSuggestedBooksItemClick: (suggestedBooksItemId: Long, isAddedLibrary: Boolean) -> Unit,
    private val onItemClick: (directions: NavDirections) -> Unit
) : BaseAdapter<ViewBinding, BooksDataModel, BaseViewHolder<BooksDataModel, ViewBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BooksDataModel, ViewBinding> {
        return SuggestedBooksItemViewHolder(
            ItemSuggestedBooksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onSuggestedBooksItemClick,
            onItemClick
        )
    }

    private class SuggestedBooksItemViewHolder(
        private val binding: ItemSuggestedBooksBinding,
        private val onSuggestedBooksItemClick: (suggestedBooksItemId: Long, isAddedLibrary: Boolean) -> Unit,
        private val onItemClick: (directions: NavDirections) -> Unit
    ) : BaseViewHolder<BooksDataModel, ViewBinding>(binding) {

        override fun bind(item: BooksDataModel, context: Context) {
            with(binding) {
                genreCategory.text = item.genre
                title.text = item.title

                saleTxt.visibility = item.sale?.let {
                    saleTxt.text = item.getSale()
                    View.VISIBLE
                } ?: View.GONE

                if (item.isAddedLibrary)
                    addLibrary.setImageResource(R.drawable.ic_added_library)
                else addLibrary.setImageResource(R.drawable.ic_add_library)

                if (item.image.isNotEmpty()) {
                    val url = GlideUrl(item.image)
                    Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.ic_placeholder)
                        .transform(RoundedCorners(context.dpToPx(R.dimen.space_4)))
                        .into(bookPicture)
                }
            }
            binding.addLibrary.setOnClickListener {
                onSuggestedBooksItemClick(item.id, !item.isAddedLibrary)
            }
        }

        override fun onItemClick(item: BooksDataModel) {
            val directions =
                ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(item.id)
            onItemClick(directions)
        }
    }
}