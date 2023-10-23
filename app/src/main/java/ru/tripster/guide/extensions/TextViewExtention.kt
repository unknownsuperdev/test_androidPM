package ru.tripster.guide.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.DrawableMarginSpan
import android.text.style.ImageSpan
import android.text.style.LeadingMarginSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.text.HtmlCompat
import ru.tripster.guide.R


open class NoUnderlineClickSpan(private val context: Context) : ClickableSpan() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = false
        textPaint.color = getColor(context, R.color.gray_100)
    }

    override fun onClick(widget: View) {}
}

fun TextView.setResizableText(
    fullText: String,
    maxLines: Int,
    viewMore: Boolean,
    isViewMoreClickable: Boolean = false,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null,
    tvWidth : Int = 0,
    action: (() -> Unit)? = null
) : Int {
    val width = if (tvWidth != 0) tvWidth else width
    if (width <= 0) {
        post {
            setResizableText(
                fullText,
                maxLines,
                viewMore,
                isViewMoreClickable,
                applyExtraHighlights,
                tvWidth
            )
        }

        return width
    }
    movementMethod = LinkMovementMethod.getInstance()
    // Since we take the string character by character, we don't want to break up the Windows-style
    // line endings.

    val adjustedText = if (viewMore) {
        fullText.replace("\n", " ")
    } else {
        fullText
    }

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
            isViewMoreClickable,
            applyExtraHighlights,
            tvWidth
        ) { action?.invoke() }
        return this.width
    }

    val charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1)
    val suffixText =
        if (viewMore) resources.getString(R.string.more) else ""
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
            isViewMoreClickable,
            applyExtraHighlights,
            tvWidth
        ) { action?.invoke() }
        return this.width
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
            isViewMoreClickable,
            applyExtraHighlights,
            tvWidth
        ) { action?.invoke() }
        return this.width
    }

    val linedText = (adjustedText.take(textLayout.getLineStart(maxLines - 1)) +
            adjustedText.substring(
                textLayout.getLineStart(maxLines - 1),
                textLayout.getLineEnd(maxLines - 1)
            ) + adjustedText.substring(textLayout.getLineEnd(maxLines - 1)))

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
    } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) || (modifier > 0 && newLayout.getLineEnd(
            maxLines - 1
        ) >= appended.length)
    )
    if (modifier > 0) {
        charactersToTake-- // We went overboard with 1 char, fixing that
    }
    // We need to convert newlines because we are going over to HTML now
    val htmlText = linedText.take(charactersToTake)
    text = addClickablePartTextResizable(
        fullText,
        maxLines,
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
        suffixText,
        viewMore,
        isViewMoreClickable,
        applyExtraHighlights,
        tvWidth
    ) {
        action?.invoke()
    }

    return this.width
}

fun TextView.checkIfResizeable(fullText: String, shortText: String) {

    val width = width

    if (width <= 0) {
        post {
            checkIfResizeable(fullText, shortText)
        }
        return
    }

    val textLayout = StaticLayout(
        fullText,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )

    this.text = if (textLayout.lineCount <= 1) fullText else shortText
}

private fun TextView.addClickablePartTextResizable(
    fullText: String,
    maxLines: Int,
    shortenedText: Spanned,
    clickableText: String?,
    viewMore: Boolean,
    isViewMoreClickable: Boolean,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null,
    tvWidth: Int,
    action: (() -> Unit)? = null
): Spannable {
    val builder = SpannableStringBuilder(shortenedText)
    if (clickableText != null) {
        builder.append(clickableText)
        val startIndexOffset = if (viewMore) 2 else 0 // Do not highlight the 3 dots and the space
        builder.setSpan(
            object : NoUnderlineClickSpan(context) {
                override fun onClick(widget: View) {
                    action?.invoke()
                    if (viewMore) {
                        setResizableText(
                            fullText,
                            maxLines,
                            false,
                            isViewMoreClickable,
                            applyExtraHighlights,
                            tvWidth
                        )
                    } else {
                        setResizableText(
                            fullText,
                            maxLines,
                            true,
                            isViewMoreClickable,
                            applyExtraHighlights,
                            tvWidth
                        )
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

fun TextView.setSpannable(
    text: String,
    coloredTest: String,
    color: Int,
    isUnderlined: Boolean = false,
    action: (() -> Unit)? = null
) {
    val spannableString = SpannableStringBuilder(text)
    val index = text.indexOf(coloredTest)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            action?.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = color
            ds.isUnderlineText = isUnderlined
        }
    }

    spannableString.setSpan(
        clickableSpan,
        index,
        index + coloredTest.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    this.text = spannableString
    this.movementMethod = LinkMovementMethod.getInstance()

}


//
//private fun TextView.addClickablePartTextResizable(
//    fullText: String,
//    maxLines: Int,
//    shortenedText: Spanned,
//    clickableText: String?,
//    viewMore: Boolean,
//    applyExtraHighlights: ((Spannable) -> (Spannable))? = null
//): Spannable {
//    val builder = SpannableStringBuilder(shortenedText)
//    if (clickableText != null) {
//        builder.append(clickableText)
//        val startIndexOffset = if (viewMore) 2 else 0 // Do not highlight the 3 dots and the space
//        builder.setSpan(
//            object : NoUnderlineClickSpan(context) {},
//            builder.indexOf(clickableText) + startIndexOffset,
//            builder.indexOf(clickableText) + clickableText.length,
//            0
//        )
//    }
//    if (applyExtraHighlights != null) {
//        return applyExtraHighlights(builder)
//    }
//    return builder
//}
//
//fun TextView.setSpannable(
//    text: String,
//    coloredTest: String,
//    color: Int,
//    isUnderlined: Boolean = false,
//    action: (() -> Unit)? = null
//) {
//    val spannableString = SpannableStringBuilder(text)
//    val index = text.indexOf(coloredTest)
//    val clickableSpan = object : ClickableSpan() {
//        override fun onClick(textView: View) {
//            action?.invoke()
//        }
//
//        override fun updateDrawState(ds: TextPaint) {
//            ds.color = color
//            ds.isUnderlineText = isUnderlined
//        }
//    }
//
//    spannableString.setSpan(
//        clickableSpan,
//        index,
//        index + coloredTest.length,
//        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//    )
//
//    this.text = spannableString
//    this.movementMethod = LinkMovementMethod.getInstance()
//
//}
//
fun AppCompatTextView.initTextLinks(
    text: String,
    context: Context,
    coloredTest: String,
    action: (() -> Unit)? = null
) {
    this.setSpannable(
        text,
        coloredTest,
        getColor(context, R.color.blue_500)
    ) {
        action?.invoke()
    }
}

fun TextView.setTypeOfStatus(drawable: Int, color: Int, title: String) {
    this.background =
        context?.getDrawable(drawable)
    context?.getColor(color)?.let { this.setTextColor(it) }
    this.text = title
}

fun TextView.setIconAndText(icon: Drawable, text: String) {
    val spannableString = SpannableString("  $text")
    icon.setBounds(0, 0, icon.intrinsicWidth + 1, icon.intrinsicHeight)
    val imageSpan = ImageSpan(icon, ImageSpan.ALIGN_BOTTOM)
    spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = spannableString
}