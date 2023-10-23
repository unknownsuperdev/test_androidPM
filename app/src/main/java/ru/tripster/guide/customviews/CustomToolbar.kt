package ru.tripster.guide.customviews

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ContextUtils.getActivity
import ru.tripster.guide.R
import ru.tripster.guide.extensions.correctColor

abstract class CustomToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    protected var customTitle: String? = null

    protected var customTitleColor: Int = -1

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)

        customTitle = attr.getString(R.styleable.CustomToolbar_title)

        customTitleColor = attr.getColor(R.styleable.CustomToolbar_titleColor, -1)

        attr.recycle()
        this.createToolbar()
    }

    abstract fun createToolbar()

    protected fun setTextViewColorInt(view: TextView, @ColorInt colorInt: Int) {
        view.setTextColor(makeSelector(colorInt))
    }

    open fun setTitleText(text: String) {}

    open fun makeSelector(color: Int): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled)
        )

        val colors = intArrayOf(color, Color.GRAY)

        return ColorStateList(states, colors)
    }
}