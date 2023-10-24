package com.name.domain.model

import com.google.gson.Gson
import com.name.entities.responce.explore.ExploreDataItemResponse
import com.name.entities.responce.explore.SuggestedBooksItemResponse
import java.util.*

data class StoryModel(
    override val id: String,
    val title: String,
    val storyList: List<StoryDataModel>,
    val position: Int = 0,
    val offset: Int = 0
) : BaseExploreDataModel(id) {

    companion object {
        fun from(data: ExploreDataItemResponse): StoryModel {
            val uuid = UUID.randomUUID().toString()
            return with(data) {
                StoryModel(
                    uuid,
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, SuggestedBooksItemResponse::class.java)
                        StoryDataModel.from(item, uuid)
                    } ?: emptyList()
                )
            }
        }
    }
}
