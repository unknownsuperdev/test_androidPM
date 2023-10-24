package com.name.jat.ui.fragments.reader

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.StyleSpan
import kotlin.math.ceil
import kotlin.math.max

class PageSplitter(
    private val pageWidth: Int = 0,
    private val pageHeight: Int = 0,
    private val lineSpacingExtra: Int = 0,
    private val typeface: Typeface
) {

    private val pages = mutableListOf<CharSequence>()
    private var currentLine = SpannableStringBuilder()
    private var currentPage = SpannableStringBuilder()
    private var currentLineHeight = 0
    private var pageContentHeight = 0
    private var currentLineWidth = 0
    private var textLineHeight = 0

    fun append(text: String, textSize: Float) {
        val textPaint = TextPaint()
        textPaint.textSize = textSize
        textPaint.typeface = typeface
        textLineHeight =
            ceil((textPaint.getFontMetrics(null) + lineSpacingExtra).toDouble())
                .toInt()
        val paragraphs = text.split("\n").dropLastWhile { it.isEmpty() }.toTypedArray()
        var i = 0
        while (i < paragraphs.size - 1) {
            appendText(paragraphs[i], textPaint)
            appendNewLine()
            i++
        }
        appendText(paragraphs[i], textPaint)
    }

    private fun appendText(text: String, textPaint: TextPaint) {
        val words = text.split(" ").dropLastWhile { it.isEmpty() }.toTypedArray()
        var i = 0
        while (i < words.size - 1) {
            appendWord(words[i] + " ", textPaint)
            i++
        }
        appendWord(words[i], textPaint)
    }

    private fun appendNewLine() {
        currentLine.append("\n")
        checkForPageEnd()
        appendLineToPage(textLineHeight)
    }

    private fun checkForPageEnd() {
        if (pageContentHeight + currentLineHeight > pageHeight) {
            pages.add(currentPage)
            currentPage = SpannableStringBuilder()
            pageContentHeight = 0
        }
    }

    private fun appendWord(appendedText: String, textPaint: TextPaint) {
        val textWidth = ceil(textPaint.measureText(appendedText).toDouble()).toInt()
        if (currentLineWidth + textWidth >= pageWidth) {
            checkForPageEnd()
            appendLineToPage(textLineHeight)
        }
        appendTextToLine(appendedText, textPaint, textWidth)
    }

    private fun appendLineToPage(textLineHeight: Int) {
        currentPage.append(currentLine)
        pageContentHeight += currentLineHeight
        currentLine = SpannableStringBuilder()
        currentLineHeight = textLineHeight
        currentLineWidth = 0
    }

    private fun appendTextToLine(appendedText: String, textPaint: TextPaint, textWidth: Int) {
        currentLineHeight = max(currentLineHeight, textLineHeight)
        currentLine.append(renderToSpannable(appendedText, textPaint))
        currentLineWidth += textWidth
    }

    fun getPages(): List<CharSequence> {
        val copyPages: MutableList<CharSequence> = ArrayList(pages)
        var lastPage = SpannableStringBuilder(currentPage)
        if (pageContentHeight + currentLineHeight > pageHeight) {
            copyPages.add(lastPage)
            lastPage = SpannableStringBuilder()
        }
        lastPage.append(currentLine)
        copyPages.add(lastPage)
        return copyPages
    }

    private fun renderToSpannable(text: String, textPaint: TextPaint): SpannableString? {
        val spannable = SpannableString(text)
        if (textPaint.isFakeBoldText) {
            spannable.setSpan(StyleSpan(Typeface.BOLD), 0, spannable.length, 0)
        }
        return spannable
    }
}