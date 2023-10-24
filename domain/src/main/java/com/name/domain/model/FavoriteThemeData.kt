package com.name.domain.model

import com.name.core.DiffUtilModel

data class FavoriteThemeData(
    override val id: Long,
    val title: String,
    val isSelected: Boolean = false
): DiffUtilModel<Long>()