package com.name.jat.extensions

import android.animation.ValueAnimator
import android.widget.TextView
import androidx.core.view.isVisible
import com.name.jat.utils.Constants

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


