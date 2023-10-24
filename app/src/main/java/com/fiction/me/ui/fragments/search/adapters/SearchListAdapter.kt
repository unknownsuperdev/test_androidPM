package com.fiction.me.ui.fragments.search.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.SearchBookWithName
import com.fiction.domain.model.SearchBookWithTag
import com.fiction.domain.model.SearchMainItem
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemSearchWithNameBinding
import com.fiction.me.databinding.ItemSearchWithTagBinding
import com.fiction.me.extensions.loadUrl

class SearchListAdapter(
    private val onClickSearchItemWithName: (book: SearchBookWithName) -> Unit,
    private val onClickSearchItemWithTag: (item: SearchBookWithTag) -> Unit
) : BaseAdapter<ViewBinding, SearchMainItem, BaseViewHolder<SearchMainItem, ViewBinding>>() {

    private val SEARCH_ITEM_WITH_NAME = 0
    private val SEARCH_ITEM_WITH_TAG = 1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SearchMainItem, ViewBinding> {
        return when (viewType) {
            SEARCH_ITEM_WITH_NAME -> SearchWithNameViewHolder(
                ItemSearchWithNameBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClickSearchItemWithName
            )
            else -> SearchWithTagViewHolder(
                ItemSearchWithTagBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClickSearchItemWithTag
            )
        }
    }

    private class SearchWithNameViewHolder(
        private val binding: ItemSearchWithNameBinding,
        private val onClickSearchItemWithName: (book: SearchBookWithName) -> Unit
    ) : BaseViewHolder<SearchMainItem, ViewBinding>(binding) {

        override fun bind(item: SearchMainItem, context: Context) {
            if (item is SearchBookWithName) {
                with(binding) {
                    bookTitle.text = item.bookName
                    caption.text = item.genres
                    bookCover.loadUrl(item.imageLink, context, R.dimen.space_4)
                }
            }
        }

        override fun onItemClick(item: SearchMainItem) {
            if (item is SearchBookWithName) {
                onClickSearchItemWithName(item)
            }
        }
    }

    private class SearchWithTagViewHolder(
        private val binding: ItemSearchWithTagBinding,
        private val onClickSearchItemWithTag: (item: SearchBookWithTag) -> Unit
    ) : BaseViewHolder<SearchMainItem, ViewBinding>(binding) {

        override fun bind(item: SearchMainItem, context: Context) {
            if (item is SearchBookWithTag) {
                with(binding) {
                    tagName.text = item.tagName
                }
            }
        }

        override fun onItemClick(item: SearchMainItem) {
            if (item is SearchBookWithTag) {
                onClickSearchItemWithTag(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchBookWithName -> SEARCH_ITEM_WITH_NAME
            else -> SEARCH_ITEM_WITH_TAG
        }
    }
}
