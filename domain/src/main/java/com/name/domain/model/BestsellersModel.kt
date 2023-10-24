package com.name.domain.model

import com.google.gson.Gson
import com.name.entities.responce.explore.ExploreDataItemResponse
import com.name.entities.responce.explore.SuggestedBooksItemResponse
import java.util.*

data class BestsellersModel(
    override val id: String,
    val title: String,
    val bestsellersList: List<BestsellersDataModel>,
) : BaseExploreDataModel(id) {

    companion object {
        fun from(data: ExploreDataItemResponse): BestsellersModel =
            with(data) {
                BestsellersModel(
                    UUID.randomUUID().toString(),
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, SuggestedBooksItemResponse::class.java)
                        BestsellersDataModel.from(item)
                    } ?: emptyList()
                )
            }
    }
}