package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetPopularTagsUseCase
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.domain.repository.TagsRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetPopularTagsUseCaseImpl(
    private val tagsRepo: TagsRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : GetPopularTagsUseCase {
    private var tags: List<PopularTagsDataModel> = emptyList()

    override suspend fun invoke(isClear: Boolean): ActionResult<List<PopularTagsDataModel>> =
        withContext(dispatcher.io) {
            if (isClear) {
                tags = emptyList()
                return@withContext ActionResult.Success(tags)
            }
            if (tags.isNotEmpty()) ActionResult.Success(tags)
            else
                when (val apiData = tagsRepo.getPopularTags()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let { response ->
                            tags = response.map { PopularTagsDataModel.from(it) }
                            ActionResult.Success(tags)
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }
}