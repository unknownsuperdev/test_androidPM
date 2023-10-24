package com.fiction.domain.model

data class BooksWithReadProgressDataModel(
    val title: String,
    val booksList: List<BooksWithReadProgressBookData>
)