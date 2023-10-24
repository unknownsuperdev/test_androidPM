package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.fiction.me.R
import com.fiction.me.databinding.ToolbarTariffsLightThemeBinding
import com.fiction.me.extensions.correctColor

class ToolBarTariffsLightTheme @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {

    private lateinit var binding: ToolbarTariffsLightThemeBinding
    private var firstIconClick: () -> Unit = {}

    override fun createToolbar() {
        binding = ToolbarTariffsLightThemeBinding.inflate(LayoutInflater.from(context), this, false)
        with(binding) {

            title.text = customTitle
            setTextViewColorInt(title, customTitleColor.correctColor(context, R.color.black))

            firstIcon.setOnClickListener {
                firstIconClick()
            }
            addView(root)
        }
    }

    override fun setTitleText(text: String) {
        binding.title.text = text
    }

    fun setLastText(text: String) {
        binding.lastText.text = text
    }

    private fun setDrawableDrawableInt(imageView: ImageView, @DrawableRes drawableId: Int) {
        imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    }

    fun setFirstIcon(@DrawableRes drawableId: Int) {
        setDrawableDrawableInt(binding.firstIcon, drawableId)
    }

    fun setLastIcon(@DrawableRes drawableId: Int) {
        setDrawableDrawableInt(binding.lastIcon, drawableId)
    }

    fun setOnFirstIconClickListener(firstIconClick: () -> Unit) {
        this.firstIconClick = firstIconClick
    }

}