package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.DataInfoResponse

data class DataInfo(
    override val id: Long,
    val name: String,
    val url: String,
    val width: Int,
    val height: Int
) : DiffUtilModel<Long>() {

    companion object {

        fun from(data: DataInfoResponse): DataInfo =
            with(data) {
                DataInfo(
                    id,
                    name,
                    url,
                    width,
                    height
                )
            }
    }
}