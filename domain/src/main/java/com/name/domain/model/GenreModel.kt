package com.name.domain.model

import com.google.gson.Gson
import com.name.entities.responce.explore.ExploreDataItemResponse
import com.name.entities.responce.explore.GenreResponse
import java.util.*

data class GenreModel (
    override val id: String,
    val title : String,
    val genreList: List<GenreDataModel>,
) : BaseExploreDataModel(id){

    companion object{
        fun from(data: ExploreDataItemResponse): GenreModel =
            with(data) {
                GenreModel(
                    UUID.randomUUID().toString(),
                    name ?: "",
                    items?.map {
                        val item = Gson().fromJson(it, GenreResponse::class.java)
                        GenreDataModel.from(item) } ?: emptyList()
                )
            }
    }
}