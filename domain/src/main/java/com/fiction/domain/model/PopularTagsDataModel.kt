package com.fiction.domain.model

import android.os.Parcelable
import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.explore.TagResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularTagsDataModel(
    override val id: Long,
    val title: String,
    val isSelected: Boolean = false
) : DiffUtilModel<Long>(), Parcelable {


    companion object {
        fun from(data: TagResponse): PopularTagsDataModel =
            with(data) {
                PopularTagsDataModel(
                    id ?: -1,
                    name ?: "",
                    false
                )
            }
    }
}
