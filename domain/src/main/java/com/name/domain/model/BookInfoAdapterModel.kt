package com.name.domain.model

import com.name.core.DiffUtilModel

data class BookInfoAdapterModel(
    override val id: Long,
    val title: String,
    val imageLink: String = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
    val bookInfo: String = "",
    val views: Int = 0,
    val likes: Int = 0,
    var isSaved: Boolean = false,
    val sale: Int? = null
) : DiffUtilModel<Long>(){

    fun getSale() = if (sale != null) "-$sale%" else null
}
