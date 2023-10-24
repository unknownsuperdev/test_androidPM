package com.name.jat.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.name.jat.R
import kotlin.math.roundToInt

open class NoUnderlineClickSpan(private val context: Context, val color: Int) : ClickableSpan() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = false
        textPaint.color = ContextCompat.getColor(context, color)
        textPaint.flags = Paint.FAKE_BOLD_TEXT_FLAG
    }

    override fun onClick(widget: View) {}
}

@SuppressLint("SetTextI18n")
@Suppress("DEPRECATION") // It has been replaced by a Builder, which is minAPI 28, so OK for now
fun TextView.setResizableText(
    fullText: String,
    maxLines: Int,
    viewMore: Boolean,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null,
    action: () -> Unit = {},
    collapseText: Boolean
) {
    val width = width
    if (width <= 0) {
        post {
            setResizableText(
                fullText,
                maxLines,
                viewMore,
                applyExtraHighlights,
                collapseText = collapseText,
                action = action
            )
        }
        return
    }
    movementMethod = LinkMovementMethod.getInstance()
    // Since we take the string character by character, we don't want to break up the Windows-style
    // line endings.
    val adjustedText = fullText.replace("\r\n", "\n")
    // Check if even the text has to be resizable.
    val textLayout = StaticLayout(
        adjustedText,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    if (textLayout.lineCount <= maxLines || adjustedText.isEmpty()) {
        // No need to add 'read more' / 'read less' since the text fits just as well (less than max lines #).
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            null,
            viewMore,
            applyExtraHighlights,
            action = action,
            collapseText = collapseText
        )
        return
    }
    val charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1)
    val suffixText =
        if (collapseText) {
            if (viewMore) resources.getString(R.string.resizable_text_read_more) else resources.getString(
                R.string.resizable_text_read_less
            )
        } else {
            resources.getString(R.string.read_more)
        }

    var charactersToTake = charactersAtLineEnd - suffixText.length / 2 // Good enough first guess
    if (charactersToTake <= 0) {
        // Happens when text is empty
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            null,
            viewMore,
            applyExtraHighlights,
            action = action,
            collapseText = collapseText
        )
        return
    }
    if (!viewMore) {
        // We can set the text immediately because nothing needs to be measured
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            suffixText,
            viewMore,
            applyExtraHighlights,
            action = action,
            collapseText = collapseText
        )
        return
    }
    val lastHasNewLine =
        adjustedText.substring(
            textLayout.getLineStart(maxLines - 1),
            textLayout.getLineEnd(maxLines - 1)
        )
            .contains("\n")
    val linedText = if (lastHasNewLine) {
        val charactersPerLine =
            textLayout.getLineEnd(0) / (textLayout.getLineWidth(0) / textLayout.ellipsizedWidth.toFloat())
        val lineOfSpaces =
            "\u00A0".repeat(charactersPerLine.roundToInt()) // non breaking space, will not be thrown away by HTML parser
        charactersToTake += lineOfSpaces.length - 1
        adjustedText.take(textLayout.getLineStart(maxLines - 1)) +
                adjustedText.substring(
                    textLayout.getLineStart(maxLines - 1),
                    textLayout.getLineEnd(maxLines - 1)
                )
                    .replace("\n", lineOfSpaces) +
                adjustedText.substring(textLayout.getLineEnd(maxLines - 1))
    } else {
        adjustedText
    }
    // Check if we perhaps need to even add characters? Happens very rarely, but can be possible if there was a long word just wrapped
    val shortenedString = linedText.take(charactersToTake)
    val shortenedStringWithSuffix = shortenedString + suffixText
    val shortenedStringWithSuffixLayout = StaticLayout(
        shortenedStringWithSuffix,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    val modifier: Int
    if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length) {
        modifier = 1
        charactersToTake-- // We might just be at the right position already
    } else {
        modifier = -1
    }
    do {
        charactersToTake += modifier
        val baseString = linedText.take(charactersToTake)
        val appended = baseString + suffixText
        val newLayout = StaticLayout(
            appended,
            paint,
            width - paddingLeft - paddingRight,
            Layout.Alignment.ALIGN_NORMAL,
            lineSpacingMultiplier,
            lineSpacingExtra,
            includeFontPadding
        )
    } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) ||
        (modifier > 0 && newLayout.getLineEnd(maxLines - 1) >= appended.length)
    )
    if (modifier > 0) {
        charactersToTake-- // We went overboard with 1 char, fixing that
    }
    // We need to convert newlines because we are going over to HTML now
    val htmlText = linedText.take(charactersToTake).replace("\n", "<br/>")
    text = addClickablePartTextResizable(
        fullText,
        maxLines,
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
        suffixText,
        viewMore,
        applyExtraHighlights,
        action = action,
        collapseText = collapseText
    )
}

private fun TextView.addClickablePartTextResizable(
    fullText: String,
    maxLines: Int,
    shortenedText: Spanned,
    clickableText: String?,
    viewMore: Boolean,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null,
    collapseText: Boolean,
    action: () -> Unit
): Spannable {
    val color = if (collapseText) {
        R.color.secondary_purple_dark_600
    } else {
        R.color.white
    }
    val builder = SpannableStringBuilder(shortenedText)
    if (clickableText != null) {
        builder.append(clickableText)
        val startIndexOffset = if (viewMore) 1 else 0 // Do not highlight the 3 dots and the space
        builder.setSpan(
            object : NoUnderlineClickSpan(context, color = color) {

                override fun onClick(widget: View) {
                    if (collapseText) {
                        if (viewMore) {
                            setResizableText(
                                fullText,
                                maxLines,
                                false,
                                applyExtraHighlights,
                                collapseText = true
                            )
                        } else {
                            setResizableText(
                                fullText,
                                maxLines,
                                true,
                                applyExtraHighlights,
                                collapseText = true
                            )
                        }
                    } else {
                        setResizableText(
                            fullText,
                            maxLines,
                            true,
                            applyExtraHighlights,
                            collapseText = false
                        )
                        action()
                    }

                }
            },
            builder.indexOf(clickableText) + startIndexOffset,
            builder.indexOf(clickableText) + clickableText.length,
            0
        )
    }
    if (applyExtraHighlights != null) {
        return applyExtraHighlights(builder)
    }
    return builder
}