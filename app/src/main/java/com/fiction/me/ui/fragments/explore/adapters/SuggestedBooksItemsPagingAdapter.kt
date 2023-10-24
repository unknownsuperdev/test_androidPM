package com.fiction.me.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BasePagingAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemSuggestedBooksBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.ui.fragments.explore.ExploreFragmentDirections

class SuggestedBooksItemsPagingAdapter(
    private val onItemClick: (id: Long, directions: NavDirections, title: String, bookId: Long?) -> Unit
) : BasePagingAdapter<ViewBinding, BookInfoAdapterModel, BaseViewHolder<BookInfoAdapterModel, ViewBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BookInfoAdapterModel, ViewBinding> {
        return SuggestedBooksItemViewHolder(
            ItemSuggestedBooksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    private class SuggestedBooksItemViewHolder(
        private val binding: ItemSuggestedBooksBinding,
        private val onItemClick: (id: Long, directions: NavDirections, title: String, bookId: Long?) -> Unit
    ) : BaseViewHolder<BookInfoAdapterModel, ViewBinding>(binding) {

        override fun bind(item: BookInfoAdapterModel, context: Context) {
            with(binding) {
                title.text = item.title

                saleTxt.visibility = item.sale?.let {
                    saleTxt.text = item.getSale()
                    View.VISIBLE
                } ?: View.GONE

                bookPicture.loadUrl(item.imageCover, context, R.dimen.space_4)
                bookPicture.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(
                            item.id,
                            bookInfo = item.bookInfo
                        )
                    onItemClick(item.id, directions, item.title, item.id)
                }

            }
        }

        override fun onItemClick(item: BookInfoAdapterModel) {
            val directions = ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(
                item.id,
                bookInfo = item.bookInfo
            )
            onItemClick(item.id, directions, item.title, item.id)
        }
    }
}
