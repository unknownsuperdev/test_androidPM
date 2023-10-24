package com.fiction.domain.model

import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.google.gson.Gson
import com.fiction.entities.response.explore.ExploreDataItemResponse
import com.fiction.entities.response.explore.GenreResponse
import java.util.*

data class GenreModel (
    override val id: String,
    val title : String,
    val genreList: List<GenreDataModel>,
) : BaseExploreDataModel(id){

    companion object{
        suspend fun from(data: ExploreDataItemResponse, useCase: GetImageFromCachingUseCase): GenreModel =
            with(data) {
                GenreModel(
                    UUID.randomUUID().toString(),
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, GenreResponse::class.java)
                        val imgCover = useCase.invoke(item.icon)
                        GenreDataModel.from(item, imgCover) } ?: emptyList()
                )
            }
    }
}