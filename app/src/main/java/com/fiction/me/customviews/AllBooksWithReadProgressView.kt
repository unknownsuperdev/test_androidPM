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
import com.fiction.domain.model.AllCurrentReadBooksDataModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.databinding.ItemAllBooksWithReadProgressListBinding
import com.fiction.me.ui.fragments.mainlibrary.adapters.AllBooksWithProgressAdapter

class AllBooksWithReadProgressView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attributeSet, defStyleAttr, defStyleRes
) {
    private var binding: ItemAllBooksWithReadProgressListBinding
    private lateinit var allCurrentReadBooksAdapter: AllBooksWithProgressAdapter

    var onItemClickListener: (id: Long, title: String) -> Unit = { _, _ -> }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.AllBooksWithReadProgressView)
        binding =
            ItemAllBooksWithReadProgressListBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
            )
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        allCurrentReadBooksAdapter = AllBooksWithProgressAdapter { id, title ->
            onItemClickListener(id, title)
        }
        with(binding) {
            bookInfoRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    adapter = allCurrentReadBooksAdapter
                }
            }
        }
    }

    fun submitData(lifecycle: Lifecycle, list: PagingData<AllCurrentReadBooksDataModel>) {
        allCurrentReadBooksAdapter.submitData(lifecycle, list)
    }

    fun observeLoadState(
        viewLifecycleOwner: LifecycleOwner,
        function: (state: LoadingState, exception: CallException?) -> Unit
    ) {
        allCurrentReadBooksAdapter.observeLoadState(viewLifecycleOwner) { state, exception ->
            function(state, exception)
        }
    }

    fun getList() = allCurrentReadBooksAdapter.snapshot().items

    fun getIsEmptyList() = allCurrentReadBooksAdapter.snapshot().items.isEmpty()
}
