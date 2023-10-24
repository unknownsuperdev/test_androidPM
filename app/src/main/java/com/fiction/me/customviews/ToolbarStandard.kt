package com.fiction.me.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fiction.me.R
import com.fiction.me.databinding.ToolbarStandardBinding
import com.fiction.me.extensions.correctColor

@SuppressLint("CustomViewStyleable")
open class ToolbarStandard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {

    private lateinit var binding: ToolbarStandardBinding

    private var customFirstItem: Drawable? = null

    private var customSecondItem: Drawable? = null

    private var firstItemClick: () -> Unit = {}

    private var backClick: () -> Unit = {}

    private var secondItemClick: () -> Unit = {}

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)

        customFirstItem = attr.getDrawable(R.styleable.CustomToolbar_firstItem)

        customSecondItem = attr.getDrawable(R.styleable.CustomToolbar_secondItem)

        attr.recycle()
        this.createToolbar()
    }

    override fun createToolbar() {
        binding = ToolbarStandardBinding.inflate(LayoutInflater.from(context), this, false)

        with(binding) {

            //*** title ***
            title.text = customTitle
            setTextViewColorInt(
                title,
                customTitleColor.correctColor(context, R.color.primary_white)
            )

            //*** firstItem element ***
            customFirstItem?.let {
                firstItem.isVisible = true
                firstItem.setImageDrawable(it)
            }

            firstItem.setOnClickListener {
                firstItemClick()
            }

            backBtn.setOnClickListener {
                backClick()
            }
            //*** secondItem element ***
            customSecondItem?.let {
                secondItem.isVisible = true
                secondItem.setImageDrawable(it)
            }

            secondItem.setOnClickListener {
                secondItemClick()
            }

            iconBadge.isVisible = false
            addView(root)
        }
    }

    private fun setDrawableDrawableInt(imageView: ImageView, @DrawableRes drawableId: Int) {
        imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    }

    override fun setTitleText(text: String) {
        binding.title.text = text
    }

    fun setBackIcon(@DrawableRes drawableId: Int) {
        setDrawableDrawableInt(binding.backBtn, drawableId)
    }

    fun setOnBackClickListener(backClick: () -> Unit) {
        this.backClick = backClick
    }

    fun setOnFirstItemClickListener(firstItemClick: () -> Unit) {
        this.firstItemClick = firstItemClick
    }

    fun setOnSecondItemClickListener(secondItemClick: () -> Unit) {
        this.secondItemClick = secondItemClick
    }

    fun setFirstItem(@DrawableRes drawableId: Int) {
        setDrawableDrawableInt(binding.firstItem, drawableId)
    }

    fun setSecondItem(@DrawableRes drawableId: Int) {
        setDrawableDrawableInt(binding.secondItem, drawableId)
    }

    fun setIconBadge(countText: String) {
        with(binding.iconBadge) {
            isVisible = true
            text = countText
        }
    }

}