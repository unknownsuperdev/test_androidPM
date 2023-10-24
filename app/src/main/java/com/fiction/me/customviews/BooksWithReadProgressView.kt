package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.me.R
import com.fiction.me.databinding.ItemBooksWithReadProgressListBinding
import com.fiction.me.ui.fragments.mainlibrary.adapters.BooksWithProgressAdapter

class BooksWithReadProgressView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attributeSet, defStyleAttr, defStyleRes
) {

    private var binding: ItemBooksWithReadProgressListBinding

    private lateinit var currentReadBookAdapter: BooksWithProgressAdapter

    var itemLongClickListener: (id: Long) -> Unit = { _ -> }
    var itemClickListener: (id: Long, title: String) -> Unit = { _, _ -> }
    var onSeeAllClickListener: () -> Unit = { }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.BooksWithReadProgressView)
        binding =
            ItemBooksWithReadProgressListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        currentReadBookAdapter = BooksWithProgressAdapter({ id ->
            itemLongClickListener(id)
        }, { id, title ->
            itemClickListener(id, title)
        })

        with(binding) {
            seeAll.isVisible = false
            title.isVisible = false
            currentReadBookRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    adapter = currentReadBookAdapter
                }
                itemAnimator = null
                isNestedScrollingEnabled = false
            }
            seeAll.setOnClickListener {
                onSeeAllClickListener()
            }
            binding.mainProgressBar.isVisible = false
        }
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    fun onClickSeeAll(seeAllClick: () -> Unit) {
        this.onSeeAllClickListener = seeAllClick
    }

    fun makeSeeAllAndTitleVisible() {
        binding.seeAll.isVisible = true
        binding.title.isVisible = true
    }

    fun submitList(list: List<BooksWithReadProgressBookData>) {
        currentReadBookAdapter.submitList(list)
    }

    fun setVisibilityForLoading(isVisible: Boolean){
        binding.mainProgressBar.isVisible = isVisible
    }
}
