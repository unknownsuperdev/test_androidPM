package com.name.domain.model

import com.name.core.DiffUtilModel

data class ChaptersModel(
    override val id: Long,
    val readingChapterPos : Int,
    val chapterList : List<ChaptersDataModel>
) :  DiffUtilModel<Long>()