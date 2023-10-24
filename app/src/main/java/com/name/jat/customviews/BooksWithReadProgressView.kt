package com.name.jat.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.domain.model.BooksWithReadProgressBookData
import com.name.jat.R
import com.name.jat.databinding.ItemBooksWithReadProgressListBinding
import com.name.jat.ui.fragments.mainlibrary.adapters.BooksWithProgressAdapter

class BooksWithReadProgressView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attributeSet, defStyleAttr, defStyleRes
) {

    private var binding: ItemBooksWithReadProgressListBinding

    private lateinit var currentReadBookAdapter: BooksWithProgressAdapter

    var itemLongClickListener: (id: Long) -> Unit = { _ -> }
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
        currentReadBookAdapter = BooksWithProgressAdapter { id ->
            itemLongClickListener(id)
        }

        with(binding) {
            seeAll.isVisible = false
            title.isVisible = false
            currentReadBookRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    adapter = currentReadBookAdapter
                }
                isNestedScrollingEnabled = false
            }
            seeAll.setOnClickListener {
                onSeeAllClickListener()
            }
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

}
