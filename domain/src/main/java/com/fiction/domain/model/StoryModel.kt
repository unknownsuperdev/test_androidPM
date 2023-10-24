package com.fiction.domain.model

import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.entities.response.explore.ExploreDataItemResponse
import com.fiction.entities.response.explore.SuggestedBooksItemResponse
import com.google.gson.Gson
import java.util.*

data class StoryModel(
    override val id: String,
    val title: String,
    val storyList: List<StoryDataModel>,
    val position: Int = 0,
    val offset: Int = 0,
    val type: FeedTypes = FeedTypes.FOR_YOU
) : BaseExploreDataModel(id) {

    companion object {
        suspend fun from(
            data: ExploreDataItemResponse,
            useCase: GetImageFromCachingUseCase
        ): StoryModel {
            val uuid = UUID.randomUUID().toString()
            return with(data) {
                StoryModel(
                    uuid,
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, SuggestedBooksItemResponse::class.java)
                        val imgHorizontal = useCase.invoke(item.covers?.imgHorizontal)
                        val imgSummary = useCase.invoke(item.covers?.imgSummary)
                        val authorAvatar = useCase.invoke(item.author?.avatar)
                        StoryDataModel.from(item, uuid, imgHorizontal, imgSummary, authorAvatar)
                    } ?: emptyList()
                )
            }
        }
    }
}
