package com.fiction.me.ui.fragments.profile.purchasehistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.profile.PurchaseHistoryData
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemPurchaseHistoryBinding

class PurchaseHistoryAdapter :
    BaseAdapter<ViewBinding, PurchaseHistoryData, BaseViewHolder<PurchaseHistoryData, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PurchaseHistoryData, ViewBinding> {
        return GenreItemViewHolder(
            ItemPurchaseHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private class GenreItemViewHolder(private val binding: ItemPurchaseHistoryBinding) :
        BaseViewHolder<PurchaseHistoryData, ViewBinding>(binding) {

        override fun bind(item: PurchaseHistoryData, context: Context) {
            with(binding) {
                title.text = item.title
                date.text = item.date
                unlockChapterCount.text = context.resources.getString(R.string._unlocked_chapters)
                    .format(item.countOfUnlockChapters)
                if (item.cover.isNotEmpty()) {
                    /*val url = GlideUrl(
                        item.cover, LazyHeaders.Builder()
                            .addHeader("User-Agent", "your-user-agent")
                            .build()
                    )
                    Glide.with(context)
                        .load(item.cover)
                        .into(bookCover)

                     */
                }
            }
        }
    }
}