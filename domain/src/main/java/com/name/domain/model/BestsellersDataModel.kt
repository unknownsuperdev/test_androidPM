package com.name.domain.model

import com.name.core.DiffUtilModel
import com.name.entities.responce.explore.SuggestedBooksItemResponse

data class BestsellersDataModel(
    override val id: Long,
    val bookTitle: String,
    val cover : String,
    val genre: String,
) : DiffUtilModel<Long>() {

    companion object {
        fun from(data: SuggestedBooksItemResponse): BestsellersDataModel =
            with(data) {
                BestsellersDataModel(
                    id ?: -1,
                    title ?: "",
                    cover ?: "",
                    genres?.map { it.name }?.reduce { result, item -> result + item } ?: ""
                )
            }
    }


}
