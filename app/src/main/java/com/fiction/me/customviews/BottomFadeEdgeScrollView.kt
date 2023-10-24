package com.fiction.me.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class BottomFadeEdgeScrollView : ScrollView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var myScrollChangeListener: OnScrollChangeListener? = null

    override fun getTopFadingEdgeStrength(): Float {
        return 0.0f
    }

    override fun getBottomFadingEdgeStrength(): Float {
        if (childCount == 0) {
            return 0.0f
        }
        return 1.0f
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (myScrollChangeListener != null) {
            if (t > oldt) {
                myScrollChangeListener?.onScrollDown()
            } else if (t < oldt) {
                myScrollChangeListener?.onScrollUp()
            }
        }
    }

    fun setScrollChangeListener(myScrollChangeListener: OnScrollChangeListener) {
        this.myScrollChangeListener = myScrollChangeListener
    }

    interface OnScrollChangeListener {
        fun onScrollUp()
        fun onScrollDown()
    }
}