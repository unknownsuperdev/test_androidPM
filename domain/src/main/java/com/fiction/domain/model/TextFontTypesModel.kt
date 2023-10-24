package com.fiction.domain.model

import com.fiction.core.DiffUtilModel
import com.fiction.domain.model.enums.TextTypeData

data class TextFontTypesModel(
    override val id: Long,
    val fontType: TextTypeData,
    val isSelected: Boolean = false
) : DiffUtilModel<Long>()