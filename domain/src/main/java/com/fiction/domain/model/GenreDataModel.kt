package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.GenreResponse

data class GenreDataModel(
    override val id: Long,
    val title: String,
    val icon: String = ""
) : DiffUtilModel<Long>() {

    companion object {
        fun from(data: GenreResponse, imgValue: String?): GenreDataModel =
            with(data) {
                GenreDataModel(
                    id ?: -1,
                    name ?: "",
                    imgValue ?: icon ?: ""
                )
            }
    }
}