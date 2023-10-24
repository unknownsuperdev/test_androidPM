package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.domain.model.BooksDataModel
import com.fiction.me.R
import com.fiction.me.databinding.ItemBooksWithReadProgressListBinding
import com.fiction.me.ui.fragments.explore.adapters.SuggestedBooksItemsAdapter

class SuggestedBooksView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attributeSet, defStyleAttr, defStyleRes
) {

    private var binding: ItemBooksWithReadProgressListBinding
    private lateinit var suggestedBooksItemsAdapter: SuggestedBooksItemsAdapter

    var onSeeAllClickListener: () -> Unit = { }
    var onSuggestedBooksItemClick: (bookId: Long, isAddedLib: Boolean) -> Unit = { _, _ -> }
    var navigateClickListener: (id: Long, title: String) -> Unit = { _, _ -> }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.SuggestedBooksView)
        binding =
            ItemBooksWithReadProgressListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        suggestedBooksItemsAdapter = SuggestedBooksItemsAdapter({ suggestedBookId, isAddedLibrary ->
            onSuggestedBooksItemClick(suggestedBookId, isAddedLibrary)
        }, { id, direction, title, ids ->
            navigateClickListener(id, title)
        })

        with(binding) {
            currentReadBookRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    adapter = suggestedBooksItemsAdapter
                }
                itemAnimator = null
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

    fun submitList(suggestBooks: List<BooksDataModel>) {
        suggestedBooksItemsAdapter.submitList(suggestBooks)
    }

    fun setVisibilityForLoading(isVisible: Boolean){
        binding.mainProgressBar.isVisible = isVisible
    }
}
