package com.fiction.me.ui.fragments.mainlibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BasePagingAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemBooksWithSwipeBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.extensions.toFormatViewsLikes
import com.fiction.me.utils.Constants
import com.fiction.me.utils.setResizableText


class BooksInfoWithSwipeAdapter(
    private val onItemSavedStateChanged: (id: Long, isSaved: Boolean, position: Int) -> Unit,
    private val onReadMoreClick: (id: Long, title: String) -> Unit,
    private val itemLongClick: (id: Long) -> Unit,
    private val deleteItemClick: (id: Long) -> Unit
) : BasePagingAdapter<ViewBinding, BookInfoAdapterModel, BaseViewHolder<BookInfoAdapterModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BookInfoAdapterModel, ViewBinding> {
        return BooksInfoWithSwipeViewHolder(
            ItemBooksWithSwipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemSavedStateChanged,
            onReadMoreClick,
            itemLongClick,
            deleteItemClick
        )
    }

    private class BooksInfoWithSwipeViewHolder(
        private val binding: ItemBooksWithSwipeBinding,
        private val onItemSavedStateChanged: (id: Long, isSaved: Boolean, position: Int) -> Unit,
        private val onReadMoreClick: (id: Long, title: String) -> Unit,
        private val itemLongClick: (id: Long) -> Unit,
        private val deleteItemClick: (id: Long) -> Unit
    ) : BaseViewHolder<BookInfoAdapterModel, ViewBinding>(binding) {

        override fun bind(item: BookInfoAdapterModel, context: Context) {
            with(binding) {
                bookItem.bookTitle.text = item.title
                val decryptionText = item.bookData
                bookItem.bookTitle.post {
                    val lineCount: Int = bookItem.bookTitle.lineCount
                    bookItem.description.maxLines = Constants.MAX_LINES_WITH_TITLE - lineCount
                    bookItem.description.text = decryptionText
                }
                /* bookItem.bookTitle.post {
                    val lineCount: Int = bookItem.bookTitle.lineCount
                   if (lineCount < Constants.MAX_LINES_WITH_TITLE)
                        bookItem.description.setResizableText(
                            decryptionText,
                            Constants.MAX_LINES_WITH_TITLE - lineCount,
                            true,
                            collapseText = false,
                            action = {
                                //onReadMoreClick(item.id, item.title)
                            })
                }*/

                bookItem.bookViews.text = item.views.toFormatViewsLikes()
                bookItem.bookLikes.text = item.likes.toFormatViewsLikes()

                if (item.isSaved)
                    bookItem.saved.setImageResource(R.drawable.ic_added_library)
                else bookItem.saved.setImageResource(R.drawable.ic_add_library)
                bookItem.bookCover.loadUrl(item.imageCover, context, R.dimen.space_4)
                bookItem.saved.setOnClickListener {
                    onItemSavedStateChanged(item.id, !item.isSaved, absoluteAdapterPosition)
                }
                bookItem.root.setOnLongClickListener {
                    itemLongClick(item.id)
                    false
                }
                bookItem.root.setOnClickListener{
                    onReadMoreClick(item.id, item.title)
                }
                delete.setOnClickListener {
                    deleteItemClick(item.id)
                }
            }
        }

        override fun onItemClick(item: BookInfoAdapterModel) {
            super.onItemClick(item)
            onReadMoreClick(item.id, item.title)
        }
    }
}
