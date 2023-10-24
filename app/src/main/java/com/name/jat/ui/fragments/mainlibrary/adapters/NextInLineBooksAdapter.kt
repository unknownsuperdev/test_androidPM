package com.name.jat.ui.fragments.mainlibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.name.domain.model.NextInLineBooksData
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemNextInLineBooksBinding

class NextInLineBooksAdapter(
    private val itemLongClick: (id: Long) -> Unit
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
            itemLongClick
        )
    }

    private class NextInLineViewHolder(
        private val binding: ItemNextInLineBooksBinding,
        private val itemLongClick: (id: Long) -> Unit
    ) : BaseViewHolder<NextInLineBooksData, ViewBinding>(binding) {

        override fun bind(item: NextInLineBooksData, context: Context) {
            with(binding) {
                title.text = item.title
                if (item.cover.isNotEmpty()) {
                    Glide.with(context)
                        .load(R.drawable.book)
                        .into(bookCover)
                }
                root.setOnLongClickListener {
                    itemLongClick(item.id)
                    true
                }
            }
        }
    }
}