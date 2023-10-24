package com.fiction.domain.model

import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.entities.response.explore.ExploreDataItemResponse
import com.fiction.entities.response.explore.SuggestedBooksItemResponse
import com.google.gson.Gson
import java.util.*

data class SuggestedBooksModel(
    val itemId: String,
    val title: String,
    val booksList: List<BooksDataModel>,
    val position: Int = 0,
    val offset: Int = 0,
    val type: FeedTypes = FeedTypes.POPULAR,
    val genreId: Int? = null
) : BaseExploreDataModel(itemId) {

    companion object {

        suspend fun from(
            data: ExploreDataItemResponse,
            useCase: GetImageFromCachingUseCase
        ): SuggestedBooksModel =
            with(data) {
                SuggestedBooksModel(
                    UUID.randomUUID().toString(),
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, SuggestedBooksItemResponse::class.java)
                        val imgCover = useCase.invoke(item.covers?.imgCover)
                        val imgSummary = useCase.invoke(item.covers?.imgSummary)
                        val authorAvatar = useCase.invoke(item.author?.avatar)
                        BooksDataModel.from(item, imgCover, imgSummary, authorAvatar)
                    } ?: emptyList(),
                    type = name?.let { getTypeFromText(it) } ?: FeedTypes.POPULAR,
                    genreId = urlCollections?.let { getGenre(it) }
                )
            }

        private fun getTypeFromText(title: String) = when (title) {
            "Popular" -> FeedTypes.POPULAR
            "Hot Romance" -> FeedTypes.HOT_ROMANCE
            "Hot Werewolf" -> FeedTypes.HOT_WEREWOLF
            "New Arrivals" -> FeedTypes.NEW_ARRIVAL
            else -> FeedTypes.SALE
        }

        private fun getGenre(url: String): Int? {
            val substring = url.substringAfter("genres")
            val genreId = substring.filter { it.isDigit() }
            return if (genreId.isNotEmpty()) genreId.toInt() else null
        }
    }

}