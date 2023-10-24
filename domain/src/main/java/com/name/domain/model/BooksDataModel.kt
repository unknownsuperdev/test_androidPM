package com.name.domain.model

import com.name.core.DiffUtilModel
import com.name.entities.responce.explore.SuggestedBooksItemResponse

data class BooksDataModel(
    override val id: Long,
    val image: String,
    val genre: String,
    val title: String,
    val isAddedLibrary: Boolean = false,
    val sale: Int? = null
) : DiffUtilModel<Long>() {

    fun getSale() = if (sale != null) "-$sale%" else null

    companion object {

        fun from(data: SuggestedBooksItemResponse): BooksDataModel =
            with(data) {
                BooksDataModel(
                    id ?: -1,
                    cover ?: "",
                    genres?.map { it.name }?.reduce { result, item -> result + item } ?: "",
                    title ?: "",
                    isAddedToLibrary ?: false,
                )
            }
    }
}