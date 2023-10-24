package com.name.domain.interactors

import com.name.domain.model.TextFontTypesModel
import com.name.domain.model.enums.TextTypeData

interface TextFontsUseCase {
    suspend operator fun invoke(selectedItemType : TextTypeData): List<TextFontTypesModel>
}
