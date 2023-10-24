package com.fiction.me.ui.fragments.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemMostPopularStoriesBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.extensions.toFormatViewsLikes
import com.fiction.me.utils.Constants.Companion.MAX_LINES_WITH_TITLE
import com.fiction.me.utils.setResizableText

class BookInfoAdapter(
    private val onItemSavedStateChanged: (id: Long, isSaved: Boolean) -> Unit,
    private val onItemClick: (id: Long, title: String) -> Unit
) : BaseAdapter<ViewBinding, BookInfoAdapterModel, BaseViewHolder<BookInfoAdapterModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BookInfoAdapterModel, ViewBinding> {
        return BookInfoViewHolder(
            ItemMostPopularStoriesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemSavedStateChanged, onItemClick
        )
    }

    private class BookInfoViewHolder(
        private val binding: ItemMostPopularStoriesBinding,
        private var onItemSavedStateChanged: (id: Long, isSaved: Boolean) -> Unit,
        private val onItemClick: (id: Long, title: String) -> Unit
    ) : BaseViewHolder<BookInfoAdapterModel, ViewBinding>(binding) {

        override fun bind(item: BookInfoAdapterModel, context: Context) {
            with(binding) {
                bookTitle.text = item.title
                val decryptionText = item.bookData
                bookTitle.post {
                    val lineCount: Int = bookTitle.lineCount
                    description.maxLines = MAX_LINES_WITH_TITLE - lineCount
                    description.text = decryptionText
                }
              /*  bookTitle.post {
                    val lineCount: Int = bookTitle.lineCount
                    if (lineCount < MAX_LINES_WITH_TITLE)
                        description.setResizableText(
                            decryptionText,
                            MAX_LINES_WITH_TITLE - lineCount,
                            true,
                            collapseText = false,
                            action = {
                                //onItemClick(item.id, item.title)
                            })
                }*/

                if (item.sale != null) {
                    saleTxt.isVisible = true
                    saleTxt.text = item.getSale()
                } else saleTxt.isVisible = false

                bookViews.text = item.views.toFormatViewsLikes()
                bookLikes.text = item.likes.toFormatViewsLikes()

                if (item.isSaved)
                    saved.setImageResource(R.drawable.ic_added_library)
                else saved.setImageResource(R.drawable.ic_add_library)

                with(bookCover) {
                    loadUrl(item.imageCover, context, R.dimen.space_4)
                    setOnClickListener {
                        onItemClick(item.id, item.title)
                    }
                }
                saved.setOnClickListener {
                    onItemSavedStateChanged(item.id, !item.isSaved)
                }
            }
        }

        override fun onItemClick(item: BookInfoAdapterModel) {
            super.onItemClick(item)
            onItemClick(item.id, item.title)
        }
    }
}
