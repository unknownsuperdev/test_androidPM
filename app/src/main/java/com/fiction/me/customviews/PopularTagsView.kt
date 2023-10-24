package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.me.R
import com.fiction.me.databinding.ItemPopularTagsListBinding
import com.fiction.me.ui.fragments.explore.adapters.PopularTagsItemsAdapter

class PopularTagsView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {

    private var binding: ItemPopularTagsListBinding

    private lateinit var popularTagsAdapter: PopularTagsItemsAdapter

    var itemClickListener: (direction: NavDirections) -> Unit = { _ -> }
    var seeAllClickListener: () -> Unit = { }
    var itemCLick: (tagId: Long, title: String) -> Unit = { _, _ -> }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.PopularTagsView)
        binding = ItemPopularTagsListBinding.inflate(LayoutInflater.from(context), this, true)
        createAdapter()
        attrs.recycle()
    }

    private fun createAdapter() {
        popularTagsAdapter = PopularTagsItemsAdapter({ direction ->
            itemClickListener(direction)
        }, { tagId, title ->
            itemCLick(tagId, title)
        })

        with(binding) {
            popularTagsRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    adapter = popularTagsAdapter
                }
                itemAnimator = null
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