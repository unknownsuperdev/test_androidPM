package com.fiction.domain.model

import com.fiction.entities.response.explore.AllTagsItemResponse
import com.fiction.entities.response.explore.ExploreDataItemResponse
import com.fiction.entities.response.explore.TagResponse
import com.google.gson.Gson
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

        fun from(data: AllTagsItemResponse/*, tagIds: List<Long>? = null*/): PopularTagsModel =
            with(data) {
                PopularTagsModel(
                    UUID.randomUUID().toString(),
                    name ?: "",
                    tags?.map {
                        PopularTagsDataModel.from(it/*, tagIds*/)
                    } ?: emptyList()
                )
            }
    }
}
