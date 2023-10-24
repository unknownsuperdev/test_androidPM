package com.name.domain.model

import com.name.entities.responce.explore.SearchBookWithNameResponse

data class SearchBookWithName(
    override val id: Long,
    val bookName: String = "",
    val contextText: String = "",
    val imageLink: String = ""
) : SearchMainItem(id) {

    companion object {

        fun from(data: SearchBookWithNameResponse): SearchBookWithName =
            with(data) {
                SearchBookWithName(
                    id,
                    bookName,
                    contextText,
                    imageLink
                )
            }
    }
}
