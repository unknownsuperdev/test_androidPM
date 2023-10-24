package com.name.jat.ui.fragments.explore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.name.domain.model.BestsellersDataModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemBestsellersBinding
import com.name.jat.extensions.dpToPx
import com.name.jat.ui.fragments.explore.ExploreFragmentDirections


class BestsellersItemsAdapter(
    private val onItemClick: (directions: NavDirections) -> Unit
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
        private val onItemClick: (directions: NavDirections) -> Unit
    ) : BaseViewHolder<BestsellersDataModel, ViewBinding>(binding) {
        @SuppressLint("CheckResult")
        override fun bind(item: BestsellersDataModel, context: Context) {
            with(binding) {
                bookTitle.text = item.bookTitle
                genreCategory.text = item.genre
                position.text = "${adapterPosition + 1}#"

                if (item.cover.isNotEmpty()) {
                    val url = GlideUrl(item.cover)
                    Glide.with(context)
                        .load(url)
                        .transform(RoundedCorners(context.dpToPx(R.dimen.space_4)))
                        .into(bookCover)
                }
            }
        }

        override fun onItemClick(item: BestsellersDataModel) {
            val directions =
                ExploreFragmentDirections.actionExploreFragmentToBookSummaryFragment(item.id)
            onItemClick(directions)
        }
    }
}