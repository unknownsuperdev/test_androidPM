package com.fiction.me.ui.fragments.retention

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.RetentionScreenBookItem
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemRetetionBinding
import com.fiction.me.extensions.loadUrl

class RetentionAdapter(
    private val onItemClick: (id: Long, title: String) -> Unit,
) : BaseAdapter<ViewBinding, RetentionScreenBookItem, BaseViewHolder<RetentionScreenBookItem, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RetentionScreenBookItem, ViewBinding> {
        return AllCurrentReadBooksViewHolder(
            ItemRetetionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    private class AllCurrentReadBooksViewHolder(
        private val binding: ItemRetetionBinding,
        private var onItemClick: (id: Long, title: String) -> Unit,
    ) : BaseViewHolder<RetentionScreenBookItem, ViewBinding>(binding) {

        override fun bind(item: RetentionScreenBookItem, context: Context) {
            with(binding) {
                title.text = item.title
                genreCategory.text = item.genre
                blurEffect.isVisible = item.isSelected
                if (item.isSelected) {
                    title.alpha = 0.5f
                    genreCategory.alpha = 0.5f
                } else {
                    title.alpha = 1f
                    genreCategory.alpha = 1f
                }
                bookCover.loadUrl(item.image, context, R.dimen.space_4)
            }
        }

        override fun onItemClick(item: RetentionScreenBookItem) {
            onItemClick(item.id, item.title)
        }
    }
}
