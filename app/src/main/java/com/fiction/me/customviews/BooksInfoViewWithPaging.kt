package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.core.CallException
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.databinding.ItemMostPopularStoriesListBinding
import com.fiction.me.ui.fragments.adapters.BookInfoPagingAdapter

class BooksInfoViewWithPaging @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
        context, attributeSet, defStyleAttr, defStyleRes
    ) {

    private var binding: ItemMostPopularStoriesListBinding
    private lateinit var bookInfoAdapter: BookInfoPagingAdapter
    var itemSaveClickListener: (list: List<BookInfoAdapterModel>, id: Long, isSaved: Boolean) -> Unit =
        { _, _, _ -> }
    var onReadMoreClickListener: (id: Long, title: String) -> Unit = { _, _ -> }
    var seeAllClickListener: () -> Unit = {}
    private var position = 0

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.BooksInfoViewWithPaging)
        binding =
            ItemMostPopularStoriesListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        bookInfoAdapter = BookInfoPagingAdapter(
            onItemSavedStateChanged = { id, isSaved, position ->
                this.position = position
                itemSaveClickListener(bookInfoAdapter.snapshot().items, id, isSaved)
            }, onReadMoreClick = { id, title ->
                onReadMoreClickListener(id, title)
            })

        with(binding) {
            title.isVisible = false
            seeAll.isVisible = false
            seeAll.setOnClickListener {
                seeAllClickListener()
            }
            bookInfoRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    adapter = bookInfoAdapter
                }
                itemAnimator = null
            }
        }
    }

    fun updateAdapter() {
        bookInfoAdapter.notifyItemChanged(position)
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    fun setSeeAllText(text: String) {
        binding.seeAll.text = text
    }

    fun makeTitleVisible() {
        binding.title.isVisible = true
    }

    fun makeSeeAllVisible() {
        binding.seeAll.isVisible = true
    }

    fun setSeeAllListener(clickListener: () -> Unit) {
        seeAllClickListener = clickListener
    }

    fun submitData(lifecycle: Lifecycle, books: PagingData<BookInfoAdapterModel>) {
        bookInfoAdapter.submitData(lifecycle, books)
    }

    fun observeLoadState(
        viewLifecycleOwner: LifecycleOwner,
        function: (state: LoadingState, exception: CallException?) -> Unit
    ) {
        bookInfoAdapter.observeLoadState(viewLifecycleOwner) { state, exception ->
            function(state, exception)
        }
    }

    fun getList() = bookInfoAdapter.snapshot().items

    fun getIsEmptyList() = bookInfoAdapter.snapshot().items.isEmpty()
}
