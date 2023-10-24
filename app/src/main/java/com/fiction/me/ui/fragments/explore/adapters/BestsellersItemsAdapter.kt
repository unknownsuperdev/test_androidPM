package com.fiction.me.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.BestsellersDataModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemBestsellersBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.ui.fragments.explore.ExploreFragmentDirections


class BestsellersItemsAdapter(
    private val onItemClick: (directions: NavDirections, title: String, bookId: Long?) -> Unit
) : BaseAdapter<ViewBinding, BestsellersDataModel, BaseViewHolder<BestsellersDataModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BestsellersDataModel, ViewBinding> {
        return BestsellerItemViewHolder(
            ItemBestsellersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    private class BestsellerItemViewHolder(
        private val binding: ItemBestsellersBinding,
        private val onItemClick: (directions: NavDirections, title: String, bookId: Long?) -> Unit
    ) : BaseViewHolder<BestsellersDataModel, ViewBinding>(binding) {

        override fun bind(item: BestsellersDataModel, context: Context) {
            with(binding) {
                bookTitle.text = item.bookTitle
                genreCategory.text = item.genre
                //position.text = "${absoluteAdapterPosition + 1}#"

                if (item.cover.isNotEmpty()) {
                    bookCover.loadUrl(item.cover, context, R.dimen.space_4)
                }
            }
        }

        override fun onItemClick(item: BestsellersDataModel) {
            val directions =
                ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(
                    item.id,
                    bookInfo = item.bookInfo
                )
            onItemClick(directions, item.bookTitle, item.id)
        }
    }
}