package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.me.R
import com.fiction.me.databinding.ItemAllPopularTagsListBinding
import com.fiction.me.ui.fragments.explore.filterbytag.AllPopularTagsItemAdapter

class AllPopularTagsView(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    private var binding: ItemAllPopularTagsListBinding
    private lateinit var allPopularTagsAdapter: AllPopularTagsItemAdapter
    var itemSelectedClickListener: (id: Long, isSelected: Boolean)-> Unit = { _, _->}

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.AllPopularTagsView)
        binding = ItemAllPopularTagsListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        allPopularTagsAdapter = AllPopularTagsItemAdapter { id, isSelected ->
            itemSelectedClickListener(id,isSelected)
        }

        with(binding) {
            allPopularTagsRv.apply {
                  context?.let {
                      layoutManager = FlexboxLayoutManager(it)
                  }
                 adapter = allPopularTagsAdapter
                }
            }
        }

        fun setTitle(title: String) {
            binding.title.text = title
        }

        fun submitList(list: List<PopularTagsDataModel>) {
            allPopularTagsAdapter.submitList(list)
        }

    fun changeVisibilityTitle(isVisible : Boolean){
        binding.title.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

}