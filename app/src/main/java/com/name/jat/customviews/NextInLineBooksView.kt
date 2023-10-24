package com.name.jat.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.domain.model.NextInLineBooksData
import com.name.jat.R
import com.name.jat.databinding.ItemNextInLineBooksListBinding
import com.name.jat.ui.fragments.mainlibrary.adapters.NextInLineBooksAdapter

class NextInLineBooksView  @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attributeSet, defStyleAttr, defStyleRes
) {

    private var binding: ItemNextInLineBooksListBinding

    private lateinit var nextInLineBooksAdapter: NextInLineBooksAdapter

    var itemLongClickListener: (id: Long) -> Unit = { _ -> }
    var onSeeAllClickListener: () -> Unit = { }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.NextInLineBooksView)
        binding =
            ItemNextInLineBooksListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        nextInLineBooksAdapter = NextInLineBooksAdapter { id ->
            itemLongClickListener(id)
        }

        with(binding) {
            currentReadBookRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    adapter = nextInLineBooksAdapter
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

    fun submitList(list: List<NextInLineBooksData>) {
        nextInLineBooksAdapter.submitList(list)
    }

}
