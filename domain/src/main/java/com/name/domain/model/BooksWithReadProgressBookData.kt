package com.name.domain.model

import com.name.core.DiffUtilModel
import com.name.entities.responce.explore.SuggestedBooksItemResponse

data class BooksWithReadProgressBookData(
    override val id: Long,
    val title: String,
    val cover: String,
    val readProgress: Int
) : DiffUtilModel<Long>() {


    companion object {
        fun from(data: SuggestedBooksItemResponse): BooksWithReadProgressBookData =
            with(data) {
                BooksWithReadProgressBookData(
                    id ?: 0,
                    title ?: "",
                    cover ?: "",
                    -1
                )
            }
    }

}
