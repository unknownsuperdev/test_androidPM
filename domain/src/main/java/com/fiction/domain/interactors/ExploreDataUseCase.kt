package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BaseExploreDataModel

interface ExploreDataUseCase {
    suspend operator fun invoke(isClear: Boolean = false): ActionResult<List<BaseExploreDataModel>>
    suspend fun updateExploreData(bookId: Long, isAddedToLib: Boolean?, isLiked: Boolean? = null, likeCount: Int? = null)
}