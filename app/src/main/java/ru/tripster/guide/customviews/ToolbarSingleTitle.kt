package ru.tripster.guide.customviews

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import ru.tripster.guide.R
import ru.tripster.guide.databinding.ToolbarSingleTitleBinding


class ToolbarSingleTitle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {

    private lateinit var binding: ToolbarSingleTitleBinding
    private var backButtonClick: () -> Unit = { (context as Activity).onBackPressed() }
    private var onFiltrationClick: () -> Unit = {}

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)

        attr.recycle()
        this.createToolbar()
    }

    override fun createToolbar() {
        binding = ToolbarSingleTitleBinding.inflate(LayoutInflater.from(context), this, false)
        with(binding) {
            title.text = customTitle
            backBtn.setOnClickListener { backButtonClick() }
            addView(root)
        }
    }

    fun setTitle(string: String) {
        binding.title.text = string
    }

}