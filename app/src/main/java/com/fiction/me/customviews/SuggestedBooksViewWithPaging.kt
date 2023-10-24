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
import com.fiction.me.databinding.ItemBooksWithReadProgressListBinding
import com.fiction.me.ui.fragments.explore.adapters.SuggestedBooksItemsPagingAdapter

class SuggestedBooksViewWithPaging @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attributeSet, defStyleAttr, defStyleRes
) {

    private var binding: ItemBooksWithReadProgressListBinding
    private lateinit var suggestedBooksItemsPagingAdapter: SuggestedBooksItemsPagingAdapter

    var onSeeAllClickListener: () -> Unit = { }
    var onSuggestedBooksItemClick: (bookId: Long, isAddedLib: Boolean) -> Unit = { _, _ -> }
    var navigateClickListener: (id: Long, title: String) -> Unit = { _, _ -> }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.SuggestedBooksPagingView)
        binding =
            ItemBooksWithReadProgressListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        suggestedBooksItemsPagingAdapter =
            SuggestedBooksItemsPagingAdapter{ id, direction, title, ids ->
                navigateClickListener(id, title)
            }

        with(binding) {
            currentReadBookRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    adapter = suggestedBooksItemsPagingAdapter
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

    fun submitData(lifecycle: Lifecycle, list: PagingData<BookInfoAdapterModel>) {
        suggestedBooksItemsPagingAdapter.submitData(lifecycle, list)
    }

    fun observeLoadState(
        viewLifecycleOwner: LifecycleOwner,
        function: (state: LoadingState, exception: CallException?) -> Unit
    ) {
        suggestedBooksItemsPagingAdapter.observeLoadState(viewLifecycleOwner) { state, exception ->
            function(state, exception)
        }
    }

    fun getList() = suggestedBooksItemsPagingAdapter.snapshot().items

    fun getIsEmptyList() = suggestedBooksItemsPagingAdapter.snapshot().items.isEmpty()

    fun onClickSeeAll(seeAllClick: () -> Unit) {
        this.onSeeAllClickListener = seeAllClick
    }

    fun setVisibilityForLoading(isVisible: Boolean) {
        binding.mainProgressBar.isVisible = isVisible
    }
}
