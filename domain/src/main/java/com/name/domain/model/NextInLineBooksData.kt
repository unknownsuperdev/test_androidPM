package com.name.domain.model

import com.name.core.DiffUtilModel

class NextInLineBooksData (
    override val id: Long,
    val title: String,
    val cover: String
) : DiffUtilModel<Long>()
