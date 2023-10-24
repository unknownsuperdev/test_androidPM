package com.name.jat.ui.fragments.mainlibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.name.domain.model.BooksWithReadProgressBookData
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemBooksWithReadProgressBinding
import com.name.jat.utils.Constants.Companion.MAX_READ_PROGRESS
import com.name.jat.utils.Constants.Companion.MIN_READ_PROGRESS

class BooksWithProgressAdapter(
    private val itemLongClick: (id: Long) -> Unit
) : BaseAdapter<ViewBinding, BooksWithReadProgressBookData, BaseViewHolder<BooksWithReadProgressBookData, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BooksWithReadProgressBookData, ViewBinding> {
        return BooksWithProgressViewHolder(
            ItemBooksWithReadProgressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemLongClick
        )
    }

    private class BooksWithProgressViewHolder(
        private val binding: ItemBooksWithReadProgressBinding,
        private val itemLongClick: (id: Long) -> Unit
    ) : BaseViewHolder<BooksWithReadProgressBookData, ViewBinding>(binding) {

        override fun bind(item: BooksWithReadProgressBookData, context: Context) {
            with(binding) {
                title.text = item.title

                if (item.readProgress > MIN_READ_PROGRESS) {
                    readProgress.isVisible = true
                    readProgress.progressDrawable = if (item.readProgress == MAX_READ_PROGRESS) {
                        ContextCompat.getDrawable(context, R.drawable.finished_progress_bar)
                    } else ContextCompat.getDrawable(context, R.drawable.read_progress)
                } else readProgress.isVisible = false
                readProgress.progress = item.readProgress

                if (item.cover.isNotEmpty()) {
                    Glide.with(context)
                        .load(item.cover)
                        .into(bookCover)
                    root.setOnLongClickListener {
                        itemLongClick(item.id)
                        true
                    }
                }
            }
        }
    }
}