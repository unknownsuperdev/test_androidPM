package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BooksDataModel

interface GetLibAlsoLikeBooksUseCase {
    suspend operator fun invoke(isMakeCallAnyway: Boolean = false, isClear: Boolean = false): ActionResult<List<BooksDataModel>>
    fun getCacheData() : List<BooksDataModel>
}