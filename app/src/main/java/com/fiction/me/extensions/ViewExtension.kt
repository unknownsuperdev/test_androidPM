package com.fiction.me.extensions

import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import com.fiction.me.R
import com.fiction.me.utils.Constants
import com.fiction.me.utils.DebounceOnClickListener

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
        topMarginDp?.run { params.topMargin = this.dpToPx(context) }
        rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
        bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
        requestLayout()
    }
}

fun View.setOnDebounceClickListener(debounceInterval: Long, listenerBlock: (View) -> Unit) =
    setOnClickListener(DebounceOnClickListener(debounceInterval, listenerBlock))

fun View.startHeaderTextAnimation(
    message: Int,
    textView: TextView,
    close: TextView,
    isSuccess: Boolean
) {
    isVisible = true
    Log.i("FictionMeIntent", "startHeaderTextAnimation: message =${message}")
    textView.setText(message)
    val startDrawable = if (isSuccess) R.drawable.ic_success else R.drawable.ic_error
    textView.setCompoundDrawablesWithIntrinsicBounds(startDrawable, 0, 0, 0)
    val valueAnimator = ValueAnimator.ofFloat(1f, 0f)
    valueAnimator.duration = Constants.CUSTOM_SNACKBAR_ANIMATION_DURATION
    valueAnimator.startDelay = 1500
    valueAnimator.addUpdateListener { animation ->
        val alphaSize = animation.animatedValue as Float
        alpha = alphaSize
    }
    close.setOnClickListener { valueAnimator.end() }
    valueAnimator.start()
}