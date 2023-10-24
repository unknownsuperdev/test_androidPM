package com.name.domain.model

import com.name.core.DiffUtilModel
import com.name.entities.responce.explore.GenreResponse

data class GenreDataModel(
    override val id: Long,
    val title: String,
    val icon: String = ""
) : DiffUtilModel<Long>() {

    companion object {
        fun from(data: GenreResponse): GenreDataModel =
            with(data) {
                GenreDataModel(
                    id ?: -1,
                    name ?: "",
                    icon ?: ""
                )
            }
    }
}