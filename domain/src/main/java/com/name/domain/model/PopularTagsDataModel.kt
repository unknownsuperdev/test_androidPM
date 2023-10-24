package com.name.domain.model

import android.os.Parcelable
import com.name.core.DiffUtilModel
import com.name.entities.responce.explore.TagResponse
import kotlinx.android.parcel.Parcelize

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
                )
            }
    }
}
