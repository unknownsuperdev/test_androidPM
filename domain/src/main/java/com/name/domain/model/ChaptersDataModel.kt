package com.name.domain.model

import com.name.core.DiffUtilModel

data class ChaptersDataModel(
    override val id: Long,
    val chapterTitle : String,
    val isLocked : Boolean,
    val isLastReadChapter: Boolean
) :  DiffUtilModel<Long>()