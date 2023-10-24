package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.core.CallException
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.databinding.ItemBooksWithSwipeListBinding
import com.fiction.me.ui.fragments.mainlibrary.adapters.BooksInfoWithSwipeAdapter

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
    var itemSaveClickListener: (books: List<BookInfoAdapterModel>, id: Long, isSaved: Boolean) -> Unit =
        { _, _, _ -> }
    var onReadMoreClickListener: (id: Long, title: String) -> Unit = { _, _ -> }
    var onDeleteClickListener: (id: Long) -> Unit = { _ -> }
    private var position = 0

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
            onItemSavedStateChanged = { id, isSaved, position ->
                this.position = position
                itemSaveClickListener(booksInfoWithSwipeAdapter.snapshot().items, id, isSaved)
            },
            onReadMoreClick = { id, title -> onReadMoreClickListener(id, title) },
            itemLongClick = { id -> itemLongClickListener(id) },
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

    fun updateAdapter() {
        booksInfoWithSwipeAdapter.notifyItemChanged(position)
    }

    fun submitData(lifecycle: Lifecycle, list: PagingData<BookInfoAdapterModel>) {
        booksInfoWithSwipeAdapter.submitData(lifecycle, list)
    }

    fun observeLoadState(
        viewLifecycleOwner: LifecycleOwner,
        function: (state: LoadingState, exception: CallException?) -> Unit
    ) {
        booksInfoWithSwipeAdapter.observeLoadState(viewLifecycleOwner) { state, exception ->
            function(state, exception)
        }
    }

    fun getList() = booksInfoWithSwipeAdapter.snapshot().items

    fun getIsEmptyList() = booksInfoWithSwipeAdapter.snapshot().items.isEmpty()
}
