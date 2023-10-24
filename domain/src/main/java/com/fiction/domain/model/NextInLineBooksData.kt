package com.fiction.domain.model

import com.fiction.core.DiffUtilModel

class NextInLineBooksData (
    override val id: Long,
    val title: String,
    val cover: String
) : DiffUtilModel<Long>()
