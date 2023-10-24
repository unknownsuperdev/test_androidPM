package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.fiction.me.R
import com.fiction.me.databinding.AppbarCustomBinding

class AppbarCustom @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet
) : AppBarLayout(context, attributeSet) {

    private var binding: AppbarCustomBinding

    var lastItemClick: () -> Unit = { }
    var navigationClickListener: () -> Unit = { }

    init {
        val attrs =
            context.obtainStyledAttributes(attributeSet, R.styleable.AppbarCustom)
        binding =
            AppbarCustomBinding.inflate(LayoutInflater.from(context), this, true)
        createAppBar()
        attrs.recycle()
    }

    private fun createAppBar() {
        with(binding) {
            collapseToolbar.setExpandedTitleTextAppearance(R.style.Headings_h1)
            collapseToolbar.setCollapsedTitleTextAppearance(R.style.Title_Medium_primary_white_18)
            lastItem.visibility = View.INVISIBLE

            lastItem.setOnClickListener {
                lastItemClick()
            }
            firstItem.setOnClickListener {
                navigationClickListener()
            }
        }
    }

    fun setTitle(title: String) {
        binding.collapseToolbar.title = title
    }

    fun makeLastItemVisible() {
        binding.lastItem.isVisible = true
    }

    fun setFirstItemDrawable(drawable: Int) {
        binding.firstItem.setImageResource(drawable)
    }

    fun setExpandedTitleTextAppearance(expandedStyle: Int) {
        binding.collapseToolbar.setExpandedTitleTextAppearance(expandedStyle)
    }

    fun setCollapsedTitleTextAppearance(collapseStyle: Int) {
        binding.collapseToolbar.setCollapsedTitleTextAppearance(collapseStyle)
    }

    fun setOnLastItemClickListener(lastItemClick: () -> Unit) {
        this.lastItemClick = lastItemClick
    }

    fun setOnNavigationClickListener(navigationItemClickListener: () -> Unit) {
        this.navigationClickListener = navigationItemClickListener
    }

}