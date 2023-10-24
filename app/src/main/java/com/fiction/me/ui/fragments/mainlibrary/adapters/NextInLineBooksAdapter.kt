package com.fiction.me.ui.fragments.mainlibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.NextInLineBooksData
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemNextInLineBooksBinding
import com.fiction.me.extensions.loadUrl

class NextInLineBooksAdapter(
    private val itemLongClick: (id: Long) -> Unit,
    private val itemClickListener: (id: Long, title: String) -> Unit
) : BaseAdapter<ViewBinding, NextInLineBooksData, BaseViewHolder<NextInLineBooksData, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<NextInLineBooksData, ViewBinding> {
        return NextInLineViewHolder(
            ItemNextInLineBooksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemLongClick,
            itemClickListener
        )
    }

    private class NextInLineViewHolder(
        private val binding: ItemNextInLineBooksBinding,
        private val itemLongClick: (id: Long) -> Unit,
        private val itemClickListener: (id: Long, title: String) -> Unit
    ) : BaseViewHolder<NextInLineBooksData, ViewBinding>(binding) {

        override fun bind(item: NextInLineBooksData, context: Context) {
            with(binding) {
                title.text = item.title
                if (item.cover.isNotEmpty()) {
                    bookCover.loadUrl(item.cover, context)
                }
                root.setOnLongClickListener {
                    itemLongClick(item.id)
                    true
                }
            }
        }

        override fun onItemClick(item: NextInLineBooksData) {
            itemClickListener(item.id, item.title)
        }
    }
}