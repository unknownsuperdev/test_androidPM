package com.fiction.domain.model

import com.fiction.entities.response.explore.SearchBookWithTagResponse
import com.fiction.entities.response.explore.SearchTagHistoryResponse

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
