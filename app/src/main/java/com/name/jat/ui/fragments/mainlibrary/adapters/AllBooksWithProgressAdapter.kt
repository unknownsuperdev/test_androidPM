package com.name.jat.ui.fragments.mainlibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.name.domain.model.AllCurrentReadBooksDataModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.utils.setResizableText
import com.name.jat.databinding.ItemAllBookWithProgressBinding
import com.name.jat.extensions.dpToPx
import com.name.jat.utils.Constants.Companion.MAX_READ_PROGRESS

class AllBooksWithProgressAdapter(
    private val onItemClick: (id: Long) -> Unit,
) : BaseAdapter<ViewBinding, AllCurrentReadBooksDataModel, BaseViewHolder<AllCurrentReadBooksDataModel, ViewBinding>>() {
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
        private var onItemClick: (id: Long) -> Unit,
    ) : BaseViewHolder<AllCurrentReadBooksDataModel, ViewBinding>(binding) {

        override fun bind(item: AllCurrentReadBooksDataModel, context: Context) {
            with(binding) {
                bookTitle.text = item.title
                readProgress.progress = item.readProgress
                readProgress.progressDrawable = if (item.readProgress == MAX_READ_PROGRESS) {
                    ContextCompat.getDrawable(context, R.drawable.finished_progress_bar)
                } else ContextCompat.getDrawable(context, R.drawable.read_progress)

                val decryptionText = item.bookInfo
                description.setResizableText(
                    decryptionText,
                    4,
                    true,
                    collapseText = false,
                    action = {
                        onItemClick(item.id)
                    })
                bookViews.text =
                    context.resources.getString(R.string.views).format(item.views)
                bookLikes.text =
                    context.resources.getString(R.string.likes).format(item.likes)

                Glide.with(context)
                    .load(R.drawable.book)
                    .transform(RoundedCorners(context.dpToPx(R.dimen.space_4)))
                    .into(bookCover)
            }
        }
    }
}
