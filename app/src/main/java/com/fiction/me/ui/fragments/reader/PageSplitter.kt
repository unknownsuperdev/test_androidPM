package com.fiction.me.ui.fragments.reader

import android.graphics.Typeface
import android.text.*
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import android.text.style.StyleSpan
import androidx.core.text.HtmlCompat
import com.fiction.domain.model.BookChapterText
import com.fiction.domain.model.BookContent
import com.fiction.domain.model.FinishedItemOfChapter
import com.fiction.domain.model.LastItemOfChapter
import kotlin.math.ceil
import kotlin.math.max

class PageSplitter(
    private val pageWidth: Int = 0,
    private val pageHeight: Int = 0,
    private val lineSpacingExtra: Int = 0,
    private val typeface: Typeface,
    private val titleTypeface: Typeface?,
) {

    private val pages = mutableListOf<CharSequence>()
    private var currentLine = SpannableStringBuilder()
    private var currentPage = SpannableStringBuilder()
    private var currentLineHeight = 0
    private var pageContentHeight = 0
    private var currentLineWidth = 0
    private var textLineHeight = 0
    private var titleLineHeight = 0
    private var isTitle = true
    private var sizeOfTitle = 19

    fun append(title: String, text: String, textSize: Float, titleSize: Float) {
        val textFromHtml = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        if (textFromHtml.isEmpty()) return
        appendTitle(title, titleSize)
        isTitle = false
        val textPaint = TextPaint()
        textPaint.textSize = textSize
        textPaint.typeface = typeface
        textLineHeight =
            ceil((textPaint.getFontMetrics(null) + lineSpacingExtra).toDouble())
                .toInt()
        val paragraphs = textFromHtml.split("\n\n").dropLastWhile { it.isEmpty() }.toTypedArray()
        var i = 0
        while (i < paragraphs.size - 1) {
            appendText(paragraphs[i], textPaint)
            appendNewLine()
            i++
        }
        appendText(paragraphs[i], textPaint)
    }

    private fun appendTitle(title: String, titleSize: Float) {
        if (title.isEmpty()) return
        sizeOfTitle = titleSize.toInt()
        val titleTextPaint = TextPaint()
        titleTextPaint.textSize = titleSize
        titleTextPaint.typeface = titleTypeface
        titleLineHeight =
            ceil((titleTextPaint.getFontMetrics(null) + lineSpacingExtra).toDouble())
                .toInt()
        appendText(title, titleTextPaint)
        appendNewLine()
        appendNewLine()
    }

    private fun appendText(text: String, textPaint: TextPaint) {
        val words = text.split(" ").dropLastWhile { it.isEmpty() }.toTypedArray()
        var i = 0
        while (i < words.size - 1) {
            if (words[i].contains("<p>"))
                appendWord(words[i].replace("<p>", "   ") + " ", textPaint)
            else appendWord(words[i] + " ", textPaint)
            i++
        }
        if (words.isNotEmpty()) appendWord(words[i], textPaint)
    }

    private fun appendNewLine() {
        currentLine.append("\n")
        checkForPageEnd()
        if (isTitle)
            appendLineToPage(titleLineHeight)
        else appendLineToPage(textLineHeight)
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
        currentLineHeight = max(currentLineHeight, if (isTitle) titleLineHeight else textLineHeight)
        currentLine.append(renderToSpannable(appendedText, textPaint))
        currentLineWidth += textWidth
    }

    fun getPages(isLastChapter: Boolean): List<BookContent> {
        val copyPages: MutableList<CharSequence> = ArrayList(pages)
        var lastPage = SpannableStringBuilder(currentPage)
        if (pageContentHeight + currentLineHeight > pageHeight) {
            copyPages.add(lastPage)
            lastPage = SpannableStringBuilder()
        }
        lastPage.append(currentLine)
        copyPages.add(lastPage)
        val allData = mutableListOf<BookContent>()
        val data: List<BookContent> = copyPages.map { BookChapterText(it) }

        allData.addAll(data)
        if (isLastChapter) {
            allData.add(FinishedItemOfChapter())
            return allData
        }
        allData.add(LastItemOfChapter())
        return allData
    }

    private fun renderToSpannable(text: String, textPaint: TextPaint): SpannableString {
        val spannable = SpannableString(text)
        if (isTitle) {
            spannable.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                titleTypeface?.let { StyleSpan(it.style) },
                0,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                AbsoluteSizeSpan(sizeOfTitle),
                0,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }
}