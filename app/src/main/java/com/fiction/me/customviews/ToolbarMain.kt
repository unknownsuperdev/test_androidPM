package com.fiction.me.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.fiction.me.R
import com.fiction.me.databinding.ToolbarMainBinding
import com.fiction.me.extensions.correctColor
import com.fiction.me.extensions.setMargins

class ToolbarMain @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {
    private lateinit var binding: ToolbarMainBinding

    private var backBtnClick: () -> Unit = {}

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ToolbarMain, 0, 0)

        attr.recycle()
        this.createToolbar()
    }

    override fun createToolbar() {
        binding = ToolbarMainBinding.inflate(LayoutInflater.from(context), this, false)

        with(binding) {

            title.text = customTitle
            setTextViewColorInt(
                title,
                customTitleColor.correctColor(context, R.color.primary_white)
            )
            backBtnContainer.setOnClickListener {
                backBtnClick()
            }
            addView(root)
        }
    }

    private fun setDrawableInt(imageView: ImageView, @DrawableRes drawableId: Int) {
        imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    }

    override fun setTitleText(text: String) {
        binding.title.text = text
    }

    fun setOnBackBtnClickListener(backBtnClick: () -> Unit) {
        this.backBtnClick = backBtnClick
    }

    fun setBackBtnIcon(@DrawableRes drawableId: Int) {
        setDrawableInt(binding.backBtn, drawableId)
    }

    fun setBackBtnStartMargin(startMargin: Int) {
        binding.backBtn.setMargins(startMargin, 0, 0, 0)
    }
}