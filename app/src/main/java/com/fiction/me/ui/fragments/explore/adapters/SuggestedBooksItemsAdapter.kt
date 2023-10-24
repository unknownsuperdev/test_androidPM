package com.fiction.me.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.BooksDataModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemSuggestedBooksBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.ui.fragments.explore.ExploreFragmentDirections

class SuggestedBooksItemsAdapter(
    private val onSuggestedBooksItemClick: (suggestedBooksItemId: Long, isAddedLibrary: Boolean) -> Unit,
    private val onItemClick: (id: Long, directions: NavDirections, title: String, bookId: Long?) -> Unit
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
        private val onItemClick: (id: Long, directions: NavDirections, title: String, bookId: Long?) -> Unit
    ) : BaseViewHolder<BooksDataModel, ViewBinding>(binding) {

        override fun bind(item: BooksDataModel, context: Context) {
            with(binding) {
                //genreCategory.text = item.genre
                title.text = item.title

                saleTxt.visibility = item.sale?.let {
                    saleTxt.text = item.getSale()
                    View.VISIBLE
                } ?: View.GONE

                if (item.isAddedLibrary)
                    addLibrary.setImageResource(R.drawable.ic_added_library)
                else addLibrary.setImageResource(R.drawable.ic_add_library)

                bookPicture.loadUrl(item.image, context, R.dimen.space_4)
                bookPicture.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(
                            item.id,
                            bookInfo = item.bookInfo
                        )
                    onItemClick(item.id, directions, item.title, item.id)
                }
            }
            binding.addLibrary.setOnClickListener {
                onSuggestedBooksItemClick(item.id, !item.isAddedLibrary)
            }
        }

        override fun onItemClick(item: BooksDataModel) {
            val directions = ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(
                item.id,
                bookInfo = item.bookInfo
            )
            onItemClick(item.id, directions, item.title, item.id)
        }
    }
}