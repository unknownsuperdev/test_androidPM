package com.fiction.me.ui.fragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BasePagingAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemMostPopularStoriesBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.extensions.toFormatViewsLikes
import com.fiction.me.utils.Constants

class BookInfoPagingAdapter(
    private val onItemSavedStateChanged: (id: Long, isSaved: Boolean, position: Int) -> Unit,
    private val onReadMoreClick: (id: Long, title: String) -> Unit
) : BasePagingAdapter<ViewBinding, BookInfoAdapterModel, BaseViewHolder<BookInfoAdapterModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BookInfoAdapterModel, ViewBinding> {
        return BookInfoViewHolder(
            ItemMostPopularStoriesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemSavedStateChanged, onReadMoreClick
        )
    }

    private class BookInfoViewHolder(
        private val binding: ItemMostPopularStoriesBinding,
        private var onItemSavedStateChanged: (id: Long, isSaved: Boolean, position: Int) -> Unit,
        private val onReadMoreClick: (id: Long, title: String) -> Unit
    ) : BaseViewHolder<BookInfoAdapterModel, ViewBinding>(binding) {

        override fun bind(item: BookInfoAdapterModel, context: Context) {
            with(binding) {
                bookTitle.text = item.title
                val decryptionText = item.bookData
                bookTitle.post {
                    val lineCount: Int = bookTitle.lineCount
                    description.maxLines = Constants.MAX_LINES_WITH_TITLE - lineCount
                    description.text = decryptionText
                }
                /*      bookTitle.post {
                          val lineCount: Int = bookTitle.lineCount
                          if (lineCount < Constants.MAX_LINES_WITH_TITLE)
                              description.setResizableText(
                                  decryptionText,
                                  Constants.MAX_LINES_WITH_TITLE - lineCount,
                                  true,
                                  collapseText = false,
                                  action = {
                                      //onReadMoreClick(item.id, item.title)
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

                bookCover.loadUrl(item.imageCover, context, R.dimen.space_4)
                saved.setOnClickListener {
                    onItemSavedStateChanged(item.id, !item.isSaved, absoluteAdapterPosition)
                }
            }
        }

        override fun onItemClick(item: BookInfoAdapterModel) {
            super.onItemClick(item)
            onReadMoreClick(item.id, item.title)
        }
    }
}
