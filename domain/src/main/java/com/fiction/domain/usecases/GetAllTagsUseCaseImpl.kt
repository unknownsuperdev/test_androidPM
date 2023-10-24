package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.GetAllTagsUseCase
import com.fiction.domain.model.PopularTagsModel
import com.fiction.domain.repository.TagsRepo
import kotlinx.coroutines.withContext

class GetAllTagsUseCaseImpl(
    private val tagsRepo: TagsRepo,
    /*private val getSelectedTagIdsUseCase: GetSelectedTagIdsUseCase,*/
    private val dispatcher: CoroutineDispatcherProvider
) : GetAllTagsUseCase {

    override suspend fun invoke(): ActionResult<List<PopularTagsModel>> =
        withContext(dispatcher.io) {
            //val selectedTagIds = getSelectedTagIdsUseCase().tagIds
            when (val apiData = tagsRepo.getAllTags()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { list ->
                        val popularTagsModel =
                            list.map { PopularTagsModel.from(it) }
                        ActionResult.Success(popularTagsModel)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}