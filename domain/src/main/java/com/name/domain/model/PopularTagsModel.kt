package com.name.domain.model

import com.google.gson.Gson
import com.name.entities.responce.explore.ExploreDataItemResponse
import com.name.entities.responce.explore.TagResponse
import java.util.*

data class PopularTagsModel(
    override val id: String,
    val title: String,
    val popularTagsList: List<PopularTagsDataModel>,
) : BaseExploreDataModel(id) {

    companion object {
        fun from(data: ExploreDataItemResponse): PopularTagsModel =
            with(data) {
                PopularTagsModel(
                    UUID.randomUUID().toString(),
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, TagResponse::class.java)
                        PopularTagsDataModel.from(item)
                    } ?: emptyList()
                )
            }
    }
}
