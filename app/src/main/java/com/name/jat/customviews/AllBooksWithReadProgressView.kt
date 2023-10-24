package com.name.jat.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.domain.model.AllCurrentReadBooksDataModel
import com.name.jat.R
import com.name.jat.databinding.ItemAllBooksWithReadProgressListBinding
import com.name.jat.ui.fragments.mainlibrary.adapters.AllBooksWithProgressAdapter

class AllBooksWithReadProgressView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attributeSet, defStyleAttr, defStyleRes
) {
    private var binding: ItemAllBooksWithReadProgressListBinding
    private lateinit var allCurrentReadBooksAdapter: AllBooksWithProgressAdapter

    var onItemClickListener: (id: Long) -> Unit = { }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.AllBooksWithReadProgressView)
        binding =
            ItemAllBooksWithReadProgressListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        allCurrentReadBooksAdapter = AllBooksWithProgressAdapter { id ->
            onItemClickListener(id)
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

    fun submitList(list: List<AllCurrentReadBooksDataModel>) {
        allCurrentReadBooksAdapter.submitList(list)
    }
}
