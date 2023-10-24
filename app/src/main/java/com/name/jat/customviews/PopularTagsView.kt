package com.name.jat.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.domain.model.PopularTagsDataModel
import com.name.jat.R
import com.name.jat.databinding.ItemPopularTagsListBinding
import com.name.jat.ui.fragments.explore.adapters.PopularTagsItemsAdapter

class PopularTagsView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {

    private var binding: ItemPopularTagsListBinding

    private lateinit var popularTagsAdapter: PopularTagsItemsAdapter

    var itemClickListener: (id: Long) -> Unit = { _ -> }
    var seeAllClickListener: () -> Unit = {  }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.PopularTagsView)
        binding = ItemPopularTagsListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    fun getRoot() = binding

    private fun createAdapter() {
        popularTagsAdapter = PopularTagsItemsAdapter { id ->
            itemClickListener(id)
        }

        with(binding) {
            popularTagsRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    adapter = popularTagsAdapter
                }
            }
            seeAll.setOnClickListener {
                seeAllClickListener()
            }
        }
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    fun setSeeAllText(text: String) {
        binding.seeAll.text = text
    }

    fun submitList(list: List<PopularTagsDataModel>) {
        popularTagsAdapter.submitList(list)
    }

    fun setOnSeeAllClickListener(seeAllClickListener: () -> Unit) {
        this.seeAllClickListener = seeAllClickListener
    }
}