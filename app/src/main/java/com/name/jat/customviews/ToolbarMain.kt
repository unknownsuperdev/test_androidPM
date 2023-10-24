package com.name.jat.customviews

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.name.jat.R
import com.name.jat.databinding.ToolbarMainBinding
import com.name.jat.extensions.correctColor

class ToolbarMain @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {
    private lateinit var binding: ToolbarMainBinding

    private var backBtnIcon: Drawable? = null
    private var backBtnClick: () -> Unit = {}

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ToolbarMain, 0, 0)

        backBtnIcon = attr.getDrawable(R.styleable.ToolbarMain_backButton)

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
            backBtnIcon?.let {
                backBtn.setImageDrawable(it)
            }
            backBtn.setOnClickListener {
                backBtnClick()
            }
            addView(root)
        }
    }

    private fun setDrawableDrawableInt(imageView: ImageView, @DrawableRes drawableId: Int) {
        imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    }

    override fun setTitleText(text: String) {
        binding.title.text = text
    }

    fun setOnBackBtnClickListener(backBtnClick: () -> Unit) {
        this.backBtnClick = backBtnClick
    }

    fun setBackBtnIcon(@DrawableRes drawableId: Int) {
        setDrawableDrawableInt(binding.backBtn, drawableId)
    }
}