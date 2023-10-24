package com.name.jat.ui.fragments.mainlibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.name.domain.model.BookInfoAdapterModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.utils.setResizableText
import com.name.jat.databinding.ItemBooksWithSwipeBinding
import com.name.jat.extensions.dpToPx

class BooksInfoWithSwipeAdapter(
    private val onItemSavedStateChanged: (id: Long, isSaved: Boolean) -> Unit,
    private val onReadMoreClick: (id: Long) -> Unit,
    private val itemLongClick: (id: Long) -> Unit,
    private val downloadItemClick: (id: Long) -> Unit,
    private val deleteItemClick: (id: Long) -> Unit
) :
    BaseAdapter<ViewBinding, BookInfoAdapterModel, BaseViewHolder<BookInfoAdapterModel, ViewBinding>>() {
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
            downloadItemClick,
            deleteItemClick
        )
    }

    private class BooksInfoWithSwipeViewHolder(
        private val binding: ItemBooksWithSwipeBinding,
        private var onItemSavedStateChanged: (id: Long, isSaved: Boolean) -> Unit,
        private val onReadMoreClick: (id: Long) -> Unit,
        private val itemLongClick: (id: Long) -> Unit,
        private val downloadItemClick: (id: Long) -> Unit,
        private val deleteItemClick: (id: Long) -> Unit
    ) : BaseViewHolder<BookInfoAdapterModel, ViewBinding>(binding) {

        override fun bind(item: BookInfoAdapterModel, context: Context) {
            with(binding) {
                bookItem.bookTitle.text = item.title
                val decryptionText = item.bookInfo
                bookItem.description.setResizableText(
                    decryptionText,
                    4,
                    true,
                    collapseText = false,
                    action = {
                        onReadMoreClick(item.id)
                    })

                bookItem.bookViews.text =
                    context.resources.getString(R.string.views_count).format(item.views)
                bookItem.bookLikes.text =
                    context.resources.getString(R.string.likes_count).format(item.likes)

                if (item.isSaved)
                    bookItem.saved.setImageResource(R.drawable.ic_added_library)
                else bookItem.saved.setImageResource(R.drawable.ic_add_library)

                Glide.with(context)
                    .load(R.drawable.book_cover)
                    .transform(RoundedCorners(context.dpToPx(R.dimen.space_4)))
                    .into(bookItem.bookCover)

                bookItem.saved.setOnClickListener {
                    onItemSavedStateChanged(item.id, !item.isSaved)
                }
                bookItem.root.setOnLongClickListener {
                    itemLongClick(item.id)
                    false
                }
                download.setOnClickListener {
                    downloadItemClick(item.id)
                }
                delete.setOnClickListener {
                    deleteItemClick(item.id)
                }
            }
        }
    }
}
