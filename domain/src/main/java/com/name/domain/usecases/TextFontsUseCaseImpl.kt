package com.name.domain.usecases

import com.name.domain.interactors.TextFontsUseCase
import com.name.domain.model.TextFontTypesModel
import com.name.domain.model.enums.TextTypeData

class TextFontsUseCaseImpl : TextFontsUseCase {
    override suspend fun invoke(selectedItemType: TextTypeData): List<TextFontTypesModel> {
        return listOf(
            TextFontTypesModel(1, TextTypeData.ROBOTO, selectedItemType == TextTypeData.ROBOTO),
            TextFontTypesModel(2, TextTypeData.PT_SERIF, selectedItemType == TextTypeData.PT_SERIF)
        )
    }
}