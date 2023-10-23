package ru.tripster.guide.customviews

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import ru.tripster.guide.R
import ru.tripster.guide.databinding.ToolbarCalendarBinding
import ru.tripster.guide.extensions.makeVisible
import ru.tripster.guide.extensions.makeVisibleGone

open class CalendarToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {

    private lateinit var binding: ToolbarCalendarBinding
    private var backButtonClick: () -> Unit = { (context as Activity).onBackPressed() }
    private var onFiltrationClick: () -> Unit = {}
    private var onCloseTimeClick: () -> Unit = {}

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)

        attr.recycle()
        this.createToolbar()
    }

    override fun createToolbar() {
        binding = ToolbarCalendarBinding.inflate(LayoutInflater.from(context), this, false)
        with(binding) {
            title.text = customTitle
            backBtn.setOnClickListener { backButtonClick() }
            filtrationBtn.setOnClickListener { onFiltrationClick() }
            closingTimeBtn.setOnClickListener { onCloseTimeClick() }
            addView(root)
        }
    }

    fun clickToFiltration(onFiltrationClick: () -> Unit) {
        this.onFiltrationClick = onFiltrationClick
    }

    fun clickToCloseTime(onCloseTimeClick: () -> Unit) {
        this.onCloseTimeClick = onCloseTimeClick
    }
    fun clickToBackBtn(onBackBtnClick: () -> Unit) {
        this.backButtonClick = onBackBtnClick
    }

    fun setTitle(string: String) {
        binding.title.text = string
    }

    fun setFilterButton(isFiltered: Boolean) {
        if (isFiltered) binding.redDot.makeVisible() else binding.redDot.makeVisibleGone()
    }

    fun visibilityOfButtons(booleanOfToolBarType: Boolean) {
        with(binding) {
            if (booleanOfToolBarType) {
                filtrationBtn.makeVisible()
                closingTimeBtn.makeVisible()
            } else {
                filtrationBtn.makeVisibleGone()
                closingTimeBtn.makeVisibleGone()
            }
        }
    }
}