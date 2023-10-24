package com.fiction.domain.model

import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.entities.response.explore.ExploreDataItemResponse
import com.fiction.entities.response.explore.SuggestedBooksItemResponse
import com.google.gson.Gson
import java.util.*

data class BestsellersModel(
    override val id: String,
    val title: String,
    val bestsellersList: List<BestsellersDataModel>,
    val type: FeedTypes,
    val genreId: Int? = null
) : BaseExploreDataModel(id) {

    companion object {
        suspend fun from(
            data: ExploreDataItemResponse,
            useCase: GetImageFromCachingUseCase,
        ): BestsellersModel =
            with(data) {
                BestsellersModel(
                    UUID.randomUUID().toString(),
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, SuggestedBooksItemResponse::class.java)
                        val imgCover = useCase.invoke(item.covers?.imgCover)
                        val imgSummary = useCase.invoke(item.covers?.imgSummary)
                        val avatar = useCase.invoke(item.author?.avatar)
                        BestsellersDataModel.from(item, imgCover, imgSummary, avatar)
                    } ?: emptyList(),
                    type = FeedTypes.TOP_WEEKLY_BESTSELLERS,
                    genreId = urlCollections?.let { getGenre(it) }
                )
            }
        private fun getGenre(url: String): Int? {
            val substring = url.substringAfter("genres")
            val genreId = substring.filter { it.isDigit() }
            return if (genreId.isNotEmpty()) genreId.toInt() else null
        }
    }
}