package com.fiction.me.ui.fragments.mainlibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemBooksWithReadProgressBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.utils.Constants

class BooksWithProgressAdapter(
    private val itemLongClick: (id: Long) -> Unit,
    private val itemClickListener: (id: Long, title: String) -> Unit
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
            itemLongClick,
            itemClickListener
        )
    }

    private class BooksWithProgressViewHolder(
        private val binding: ItemBooksWithReadProgressBinding,
        private val itemLongClick: (id: Long) -> Unit,
        private val itemClickListener: (id: Long, title: String) -> Unit
    ) : BaseViewHolder<BooksWithReadProgressBookData, ViewBinding>(binding) {

        override fun bind(item: BooksWithReadProgressBookData, context: Context) {
            with(binding) {
                title.text = item.title

                if (item.readProgress > Constants.MIN_READ_PROGRESS) {
                    readProgress.isVisible = true
                    readProgress.progressDrawable =
                        if (item.readProgress == Constants.MAX_READ_PROGRESS) {
                            ContextCompat.getDrawable(context, R.drawable.finished_progress_bar)
                        } else ContextCompat.getDrawable(context, R.drawable.read_progress)
                } else readProgress.isVisible = false
                readProgress.progress = item.readProgress

                if (item.cover.isNotEmpty()) {
                    bookCover.loadUrl(item.cover, context)
                }
                root.setOnLongClickListener {
                    itemLongClick(item.id)
                    true
                }
            }
        }

        override fun onItemClick(item: BooksWithReadProgressBookData) {
            itemClickListener(item.id, item.title)
        }
    }
}