package ru.tripster.guide.customviews

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.isVisible
import ru.tripster.guide.R
import ru.tripster.guide.databinding.ToolbarDoubleTitleBinding

@SuppressLint("CustomViewStyleable")
open class ToolbarDoubleTitle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {

    private lateinit var binding: ToolbarDoubleTitleBinding

    private var customSubTitle: String? = null

    private var backButtonClick: () -> Unit = { (context as Activity).onBackPressed() }

    private var phoneNumberClick: () -> Unit = {}

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)

        customSubTitle = attr.getString(R.styleable.CustomToolbar_subTitle)

        attr.recycle()
        this.createToolbar()
    }

    override fun createToolbar() {
        binding = ToolbarDoubleTitleBinding.inflate(LayoutInflater.from(context), this, false)

        with(binding) {
            title.text = customTitle
            subTitle.text = customSubTitle
            customSubTitle?.let { setSubTitle(it) }
            phone.setOnClickListener { phoneNumberClick() }
            backBtn.setOnClickListener { backButtonClick() }
            addView(root)
        }
    }

    fun setSubTitle(string: String) {
        binding.subTitle.text = string
    }

    fun setPhoneVisibility(isVisible: Boolean) {
        binding.phone.isVisible = isVisible
    }

    fun onPhoneClick(phoneNumberClick: () -> Unit) {
        this.phoneNumberClick = phoneNumberClick
    }

    override fun setTitleText(text: String) {
        binding.title.text = text
    }

}