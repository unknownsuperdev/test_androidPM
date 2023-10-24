package com.name.domain.model

import com.name.core.DiffUtilModel

data class BookInfoAdapterModelList(
    override val id: Long,
    val title: String,
    val list: List<BookInfoAdapterModel>
) : DiffUtilModel<Long>()