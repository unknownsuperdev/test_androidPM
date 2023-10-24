package com.name.domain.model

import com.name.core.DiffUtilModel

class AllCurrentReadBooksDataModel(
    override val id: Long,
    val title: String,
    val readProgress: Int,
    val bookInfo: String = "",
    val imageLink: String = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
    val views: Int = 30,
    val likes: Int = 14
) : DiffUtilModel<Long>()