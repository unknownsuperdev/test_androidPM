package com.name.domain.model

import com.name.entities.responce.explore.SearchBookWithTagResponse
import com.name.entities.responce.explore.SearchTagHistoryResponse

data class SearchBookWithTag(
    override val id: Long,
    val tagName: String,
) : SearchMainItem(id) {

    companion object {

        fun from(data: SearchBookWithTagResponse): SearchBookWithTag =
            with(data) {
                SearchBookWithTag(
                    id ?: 0,
                    tagName ?: ""
                )
            }

        fun fromHistory(data: SearchTagHistoryResponse): SearchBookWithTag =
            with(data) {
                SearchBookWithTag(
                    id = 0,
                    tagName ?: ""
                )
            }
    }
}
