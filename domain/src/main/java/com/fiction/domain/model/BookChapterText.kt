package com.fiction.domain.model

data class BookChapterText(
     val chapterPage: CharSequence,
     override val id: Int = BookChapterText::class.hashCode()
) : BookContent()
