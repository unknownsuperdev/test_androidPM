package ru.tripster.guide.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import ru.tripster.guide.R
import ru.tripster.guide.databinding.AppbarCustomBinding

class AppbarCustom @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet
) : AppBarLayout(context, attributeSet) {

    private var binding: AppbarCustomBinding

    var leftViewClickListener: () -> Unit = { }

    init {
        binding =
            AppbarCustomBinding.inflate(LayoutInflater.from(context), this, true)
        createAppBar()
    }

    private fun createAppBar() {
        with(binding) {
            root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            collapseToolbar.setExpandedTitleTextAppearance(R.style.Text_32_700)
            collapseToolbar.setCollapsedTitleTextAppearance(R.style.Text_32_700)
            collapseToolbar.expandedTitleMarginStart =
                resources.getDimension(R.dimen.space_16).toInt()

            firstItem.setOnClickListener {
                leftViewClickListener()
            }
        }
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    fun setScrollFlag (isScrollable : Boolean) {
        val collapsingToolbar = binding.root
        val params = collapsingToolbar.layoutParams as LayoutParams
        params.scrollFlags = if (isScrollable) LayoutParams.SCROLL_FLAG_SCROLL else LayoutParams.SCROLL_FLAG_NO_SCROLL
        collapsingToolbar.layoutParams = params
    }

    fun setFirstItemDrawable(drawable: Int) {
        binding.firstItem.setImageResource(drawable)
    }

    fun setExpandedTitleTextAppearance(expandedStyle: Int) {
        binding.collapseToolbar.setExpandedTitleTextAppearance(expandedStyle)
    }

    fun setTitle(text: Int) {
        binding.root.title = context.resources.getString(text)
    }

    fun setCollapsedTitleTextAppearance(collapseStyle: Int) {
        binding.collapseToolbar.setCollapsedTitleTextAppearance(collapseStyle)
    }

    fun setOnNavigationClickListener(leftViewClickListener: () -> Unit) {
        this.leftViewClickListener = leftViewClickListener
    }

}