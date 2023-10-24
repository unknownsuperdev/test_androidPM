package com.name.domain.usecases

import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.interactors.BookSettingsGetUseCase
import com.name.domain.model.BookSettingsData
import com.name.domain.model.enums.ColorThemeData
import com.name.domain.model.enums.FlipTypeData
import com.name.domain.model.enums.LineHeightData
import com.name.domain.model.enums.TextTypeData
import com.name.domain.repository.BookSettingsRepo
import kotlinx.coroutines.withContext
import java.util.*

class BookSettingsGetUseCaseImpl(
    private val bookSettingsRepo: BookSettingsRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : BookSettingsGetUseCase {

    override suspend fun invoke() = withContext(dispatcher.io) {
        bookSettingsRepo.getBookSettingsData()?.let {
            BookSettingsData.from(it)
        } ?: BookSettingsData(
            UUID.randomUUID().toString(),
            0,
            ColorThemeData.BLACK,
            TextTypeData.PT_SERIF,
            15,
            LineHeightData.DEFAULT,
            FlipTypeData.SCROLL
        )
    }
}