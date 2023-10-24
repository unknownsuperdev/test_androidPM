package com.fiction.me.ui.fragments.mainlibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.AllCurrentReadBooksDataModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BasePagingAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemAllBookWithProgressBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.extensions.toFormatViewsLikes
import com.fiction.me.utils.Constants
import com.fiction.me.utils.Constants.Companion.MAX_READ_PROGRESS
import com.fiction.me.utils.setResizableText

class AllBooksWithProgressAdapter(
    private val onItemClick: (id: Long, title: String) -> Unit,
) : BasePagingAdapter<ViewBinding, AllCurrentReadBooksDataModel, BaseViewHolder<AllCurrentReadBooksDataModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<AllCurrentReadBooksDataModel, ViewBinding> {
        return AllCurrentReadBooksViewHolder(
            ItemAllBookWithProgressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    private class AllCurrentReadBooksViewHolder(
        private val binding: ItemAllBookWithProgressBinding,
        private var onItemClick: (id: Long, title: String) -> Unit,
    ) : BaseViewHolder<AllCurrentReadBooksDataModel, ViewBinding>(binding) {

        override fun bind(item: AllCurrentReadBooksDataModel, context: Context) {
            with(binding) {
                bookTitle.text = item.title
                readProgress.progress = item.readProgress
                readProgress.progressDrawable = if (item.readProgress == MAX_READ_PROGRESS) {
                    ContextCompat.getDrawable(context, R.drawable.finished_progress_bar)
                } else ContextCompat.getDrawable(context, R.drawable.read_progress)
                val decryptionText = item.bookData
                bookTitle.post {
                    val lineCount: Int = bookTitle.lineCount
                    description.maxLines = Constants.MAX_LINES_WITH_TITLE - lineCount
                    description.text = decryptionText
                }

       /*         bookTitle.post {
                    val lineCount: Int = bookTitle.lineCount
                    if (lineCount < Constants.MAX_LINES_WITH_TITLE)
                        description.setResizableText(
                            decryptionText,
                            Constants.MAX_LINES_WITH_TITLE - lineCount,
                            true,
                            collapseText = false,
                            action = {
                                //onItemClick(item.id, item.title)
                            })
                }*/

                bookViews.text = item.views.toFormatViewsLikes()
                bookLikes.text = item.likes.toFormatViewsLikes()
                bookCover.loadUrl(item.imageLink, context, R.dimen.space_4)
            }
        }

        override fun onItemClick(item: AllCurrentReadBooksDataModel) {
            onItemClick(item.id, item.title)
        }
    }
}
