package com.fiction.me.extensions

import android.animation.ValueAnimator
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.fiction.me.utils.Constants

fun TextView.startHeaderTextAnimation(message: Int) {
    isVisible = true
    setText(message)
    val valueAnimator = ValueAnimator.ofFloat(1f, 0f)
    valueAnimator.duration = Constants.CUSTOM_SNACKBAR_ANIMATION_DURATION
    valueAnimator.addUpdateListener { animation ->
        val alphaSize = animation.animatedValue as Float
        alpha = alphaSize
    }
    valueAnimator.start()
}

fun TextView.startHeaderTextAnimation(message: String) {
    isVisible = true
    text = message
    val valueAnimator = ValueAnimator.ofFloat(1f, 0f)
    valueAnimator.duration = Constants.CUSTOM_SNACKBAR_ANIMATION_DURATION
    valueAnimator.addUpdateListener { animation ->
        val alphaSize = animation.animatedValue as Float
        alpha = alphaSize
    }
    valueAnimator.start()
}

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
        if (startIndexOfLink == -1) continue
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}


