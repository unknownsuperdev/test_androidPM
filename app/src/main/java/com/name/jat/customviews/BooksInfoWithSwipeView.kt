package com.name.jat.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.domain.model.BookInfoAdapterModel
import com.name.jat.R
import com.name.jat.databinding.ItemBooksWithSwipeListBinding
import com.name.jat.ui.fragments.mainlibrary.adapters.BooksInfoWithSwipeAdapter

class BooksInfoWithSwipeView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(
        context, attributeSet, defStyleAttr, defStyleRes
    ) {

    private var binding: ItemBooksWithSwipeListBinding
    private lateinit var booksInfoWithSwipeAdapter: BooksInfoWithSwipeAdapter

    var itemLongClickListener: (id: Long) -> Unit = { _ -> }
    var itemSaveClickListener: (id: Long, isSaved: Boolean) -> Unit = { _, _ -> }
    var onReadMoreClickListener: (id: Long) -> Unit = { _ -> }
    var onDownloadClickListener: (id: Long) -> Unit = { _ -> }
    var onDeleteClickListener: (id: Long) -> Unit = { _ -> }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.BooksInfoWithSwipeView)
        binding =
            ItemBooksWithSwipeListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        booksInfoWithSwipeAdapter = BooksInfoWithSwipeAdapter(
            onItemSavedStateChanged = { id, isSaved -> itemSaveClickListener(id, isSaved) },
            onReadMoreClick = { id -> onReadMoreClickListener(id) },
            itemLongClick = { id -> itemLongClickListener(id) },
            downloadItemClick = { id -> onDownloadClickListener(id) },
            deleteItemClick = { id -> onDeleteClickListener(id) }
        )

        with(binding) {
            bookInfoRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    adapter = booksInfoWithSwipeAdapter
                }
                itemAnimator = null
            }
        }
    }

    fun submitList(list: List<BookInfoAdapterModel>) {
        booksInfoWithSwipeAdapter.submitList(list)
    }
}
