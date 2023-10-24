package com.name.jat.ui.fragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.name.domain.model.BookInfoAdapterModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemMostPopularStoriesBinding
import com.name.jat.extensions.dpToPx
import com.name.jat.utils.Constants.Companion.MAX_LINES
import com.name.jat.utils.setResizableText

class BookInfoAdapter(
    private val onItemSavedStateChanged: (id: Long, isSaved: Boolean) -> Unit,
    private val onReadMoreClick: (id: Long) -> Unit
) :
    BaseAdapter<ViewBinding, BookInfoAdapterModel, BaseViewHolder<BookInfoAdapterModel, ViewBinding>>() {
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
        private var onItemSavedStateChanged: (id: Long, isSaved: Boolean) -> Unit,
        private val onReadMoreClick: (id: Long) -> Unit
    ) : BaseViewHolder<BookInfoAdapterModel, ViewBinding>(binding) {

        override fun bind(item: BookInfoAdapterModel, context: Context) {
            with(binding) {
                bookTitle.text = item.title
                val decryptionText = item.bookInfo
                description.setResizableText(
                    decryptionText,
                    MAX_LINES,
                    true,
                    collapseText = false,
                    action = {
                        onReadMoreClick(item.id)
                    })

                if (item.sale != null) {
                    saleTxt.isVisible = true
                    saleTxt.text = item.getSale()
                } else saleTxt.isVisible = false

                bookViews.text =
                    context.resources.getString(R.string.views_count).format(item.views)
                bookLikes.text =
                    context.resources.getString(R.string.likes_count).format(item.likes)

                if (item.isSaved)
                    saved.setImageResource(R.drawable.ic_added_library)
                else saved.setImageResource(R.drawable.ic_add_library)

                Glide.with(context)
                    .load(R.drawable.book_cover)
                    .transform(RoundedCorners(context.dpToPx(R.dimen.space_4)))
                    .into(bookCover)

                saved.setOnClickListener {
                    onItemSavedStateChanged(item.id, !item.isSaved)
                }
            }
        }
    }
}
