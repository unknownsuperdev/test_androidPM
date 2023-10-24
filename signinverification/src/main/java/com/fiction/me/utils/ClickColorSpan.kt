package com.fiction.me.utils

import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.view.View
import androidx.compose.ui.graphics.BlendMode.Companion.Color
/*


class ClickColorSpan(private val onClick: () -> Unit) : CharacterStyle(), ClickableSpan() {

    private var isClicked = false

    override fun updateDrawState(ds: TextPaint) {
        if (isClicked) {
            ds.color = android.graphics.Color.RED// Change the color of the text when clicked
        }
    }

    override fun onClick(widget: View) {
        isClicked = true // Set the clicked flag to true
        onClick() // Call the click handler
    }
}*/
