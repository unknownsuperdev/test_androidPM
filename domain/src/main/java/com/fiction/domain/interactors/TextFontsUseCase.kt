package com.fiction.domain.interactors

import com.fiction.domain.model.TextFontTypesModel
import com.fiction.domain.model.enums.TextTypeData

interface TextFontsUseCase {
    suspend operator fun invoke(selectedItemType : TextTypeData): List<TextFontTypesModel>
}
