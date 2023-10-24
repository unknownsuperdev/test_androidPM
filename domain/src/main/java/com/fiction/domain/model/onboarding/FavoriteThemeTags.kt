package com.fiction.domain.model.onboarding

import com.fiction.core.DiffUtilModel
import com.fiction.entities.response.onboarding.FavoriteThemeTagResponse

data class FavoriteThemeTags(
    override val id: Long,
    val title: String,
    val isSelected: Boolean = false
) : DiffUtilModel<Long>() {


    companion object {
        fun from(data: FavoriteThemeTagResponse, selectedFavoriteThemeTags: List<Long>?): FavoriteThemeTags =
            with(data) {
                FavoriteThemeTags(
                    id ?: 0,
                    name ?: "",
                    selectedFavoriteThemeTags?.contains(id) ?: false
                )
            }
    }
}
