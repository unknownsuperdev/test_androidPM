package com.name.domain.model

import com.name.core.DiffUtilModel
import com.name.domain.model.enums.TextTypeData

data class TextFontTypesModel(
    override val id: Long,
    val fontType: TextTypeData,
    val isSelected: Boolean = false
) : DiffUtilModel<Long>()