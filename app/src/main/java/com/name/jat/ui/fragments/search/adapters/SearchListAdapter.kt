package com.name.jat.ui.fragments.search.adapters

import android.content.*
import android.view.*
import androidx.viewbinding.*
import com.bumptech.glide.*
import com.bumptech.glide.load.resource.bitmap.*
import com.name.domain.model.*
import com.name.jat.R
import com.name.jat.appbase.adapter.*
import com.name.jat.databinding.*
import com.name.jat.extensions.*

class SearchListAdapter(
	private val onClickSearchItemWithName: (book: SearchBookWithName) -> Unit,
	private val onClickSearchItemWithTag: (item: SearchBookWithTag) -> Unit
) :
	BaseAdapter<ViewBinding, SearchMainItem, BaseViewHolder<SearchMainItem, ViewBinding>>() {

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
	) :
		BaseViewHolder<SearchMainItem, ViewBinding>(binding) {

		override fun bind(item: SearchMainItem, context: Context) {
			if (item is SearchBookWithName) {
				with(binding) {
					bookTitle.text = item.bookName
					caption.text = item.contextText
					Glide.with(context)
						.load(R.drawable.book_cover)
						.transform(RoundedCorners(context.dpToPx(R.dimen.space_4)))
						.into(bookCover)
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
	) :
		BaseViewHolder<SearchMainItem, ViewBinding>(binding) {

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
