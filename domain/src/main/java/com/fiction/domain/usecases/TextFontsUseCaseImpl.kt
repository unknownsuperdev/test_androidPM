package com.fiction.domain.usecases

import com.fiction.domain.interactors.TextFontsUseCase
import com.fiction.domain.model.TextFontTypesModel
import com.fiction.domain.model.enums.TextTypeData

class TextFontsUseCaseImpl : TextFontsUseCase {
    override suspend fun invoke(selectedItemType: TextTypeData): List<TextFontTypesModel> {
        return listOf(
            TextFontTypesModel(1, TextTypeData.ROBOTO, selectedItemType == TextTypeData.ROBOTO),
            TextFontTypesModel(2, TextTypeData.PT_SERIF, selectedItemType == TextTypeData.PT_SERIF)
        )
    }
}