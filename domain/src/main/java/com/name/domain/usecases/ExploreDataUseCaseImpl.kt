package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants.Companion.ERROR_NULL_DATA
import com.name.domain.interactors.ExploreDataUseCase
import com.name.domain.model.*
import com.name.domain.repository.ExploreDataRepository
import kotlinx.coroutines.withContext

class ExploreDataUseCaseImpl(
    private val exploreDataRepo: ExploreDataRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ExploreDataUseCase {
    override suspend fun invoke(): ActionResult<List<BaseExploreDataModel>> {
        val baseExploreDataList = mutableListOf<BaseExploreDataModel>()

        return withContext(dispatcher.io) {
            when (val apiData = exploreDataRepo.getExploreData()) {
                is ActionResult.Success -> {

                    apiData.result.data?.let { response ->
                        response.forEach {
                            when {
                                it.type == "books" && it.name == "For you" -> baseExploreDataList.add(
                                    StoryModel.from(it)
                                )
                                it.type == "books" && it.name == "Top Weekly Bestsellers" -> baseExploreDataList.add(
                                    BestsellersModel.from(it)
                                )
                                it.type == "books" -> baseExploreDataList.add(
                                    SuggestedBooksModel.from(
                                        it
                                    )
                                )
                                it.type == "genres" -> baseExploreDataList.add(GenreModel.from(it))
                                it.type == "tags" -> baseExploreDataList.add(
                                    PopularTagsModel.from(
                                        it
                                    )
                                )
                            }
                        }
                        ActionResult.Success(baseExploreDataList)
                    } ?: ActionResult.Error(CallException(ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
    }
}