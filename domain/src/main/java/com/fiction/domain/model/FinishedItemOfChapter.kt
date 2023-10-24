package com.fiction.domain.model

data class FinishedItemOfChapter(override val id: Int = LastItemOfChapter::class.hashCode()) : BookContent()
