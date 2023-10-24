package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.R
import com.fiction.me.databinding.ItemMostPopularStoriesListBinding
import com.fiction.me.ui.fragments.adapters.BookInfoAdapter

class BooksInfoView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(
        context, attributeSet, defStyleAttr, defStyleRes
    ) {

    private var binding: ItemMostPopularStoriesListBinding

    private lateinit var bookInfoAdapter: BookInfoAdapter


    var itemSaveClickListener: (id: Long, isSaved: Boolean) -> Unit = { _, _ -> }
    var onReadMoreClickListener: (id: Long, title: String) -> Unit = { _, _ -> }
    var seeAllClickListener: () -> Unit = {}

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.BooksInfoView)
        binding =
            ItemMostPopularStoriesListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        bookInfoAdapter = BookInfoAdapter(
            onItemSavedStateChanged = { id, isSaved ->
                itemSaveClickListener(id, isSaved)
            }, onItemClick = { id, title ->
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

    fun submitList(list: List<BookInfoAdapterModel>) {
        bookInfoAdapter.submitList(list)
    }
}
