package com.name.domain.model

import com.google.gson.Gson
import com.name.entities.responce.explore.ExploreDataItemResponse
import com.name.entities.responce.explore.SuggestedBooksItemResponse
import java.util.*

data class SuggestedBooksModel(
    val itemId: String,
    val title: String,
    val booksList: List<BooksDataModel>,
    val position: Int = 0,
    val offset: Int = 0
) : BaseExploreDataModel(itemId) {

    companion object {

        fun from(data: ExploreDataItemResponse): SuggestedBooksModel =
            with(data) {
                SuggestedBooksModel(
                    UUID.randomUUID().toString(),
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, SuggestedBooksItemResponse::class.java)
                        BooksDataModel.from(item)
                    } ?: emptyList()
                )
            }
    }

}