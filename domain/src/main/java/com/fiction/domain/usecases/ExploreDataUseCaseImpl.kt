package com.fiction.domain.usecases

import android.content.Context
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.SetImageCacheUseCase
import com.fiction.domain.model.*
import com.fiction.domain.repository.ExploreDataRepository
import com.fiction.domain.utils.Constants.Companion.ERROR_NULL_DATA
import kotlinx.coroutines.withContext

class ExploreDataUseCaseImpl(
    private val exploreDataRepo: ExploreDataRepository,
    private val getImageFromCachingUseCase: GetImageFromCachingUseCase,
    private val cachingBookImagesUseCase: CachingBookImagesUseCase,
    private val setImageCacheUseCase: SetImageCacheUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : ExploreDataUseCase {
    private var baseExploreDataList = mutableListOf<BaseExploreDataModel>()

    override suspend fun invoke( isClear: Boolean): ActionResult<List<BaseExploreDataModel>> {
        return withContext(dispatcher.io) {
            if (isClear) {
                baseExploreDataList = mutableListOf()
                return@withContext ActionResult.Success(emptyList())
            }
            if (baseExploreDataList.isNotEmpty()) ActionResult.Success(baseExploreDataList)
            else {
                when (val apiData = exploreDataRepo.getExploreData()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let { response ->
                            response.forEach { responseItem ->
                                when {
                                    responseItem.type == "books" && responseItem.name == "For you" -> {
                                        val storyModel = StoryModel.from(
                                            responseItem,
                                            getImageFromCachingUseCase
                                        )
                                        cachingBookImagesUseCase.invoke(storyModel.storyList.map { it.picture })
                                        storyModel.storyList.forEach {
                                            setImageCacheUseCase(it.picture)
                                        }
                                        baseExploreDataList.add(
                                            storyModel
                                        )
                                    }
                                    responseItem.type == "books" && responseItem.name == "Top Weekly Bestsellers" -> {
                                        val bestsellers = BestsellersModel.from(
                                            responseItem,
                                            getImageFromCachingUseCase
                                        )
                                        cachingBookImagesUseCase.invoke(bestsellers.bestsellersList.map { it.cover })
                                        bestsellers.bestsellersList.forEach {
                                            setImageCacheUseCase(it.cover)
                                        }
                                        baseExploreDataList.add(
                                            bestsellers
                                        )
                                    }
                                    responseItem.type == "books" -> {
                                        val suggestedBooksModel = SuggestedBooksModel.from(
                                            responseItem,
                                            getImageFromCachingUseCase
                                        )
                                        cachingBookImagesUseCase.invoke(suggestedBooksModel.booksList.map { it.image })
                                        suggestedBooksModel.booksList.forEach {
                                            setImageCacheUseCase(it.image)
                                        }
                                        baseExploreDataList.add(
                                            suggestedBooksModel
                                        )
                                    }
                                    responseItem.type == "genres" -> {
                                        val genres = GenreModel.from(
                                            responseItem,
                                            getImageFromCachingUseCase
                                        )
                                        cachingBookImagesUseCase.invoke(genres.genreList.map { it.icon })
                                        genres.genreList.forEach {
                                            setImageCacheUseCase(it.icon)
                                        }
                                        baseExploreDataList.add(
                                            genres
                                        )
                                    }
                                    responseItem.type == "tags" -> baseExploreDataList.add(
                                        PopularTagsModel.from(
                                            responseItem
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

    override suspend fun updateExploreData(
        bookId: Long,
        isAddedToLib: Boolean?,
        isLiked: Boolean?,
        likeCount: Int?
    ) {
        val updatedList = baseExploreDataList.toMutableList()
        for (i in updatedList.indices) {
            when (updatedList[i]) {
                is StoryModel -> {
                    val itemStory = updatedList[i] as StoryModel
                    val storyList =
                        itemStory.storyList.map {
                            if (bookId == it.id) {
                                it.copy(
                                    isAddedLibrary = isAddedToLib ?: it.isAddedLibrary,
                                    bookInfo = it.bookInfo.copy(isAddedInLib = isAddedToLib?: it.isAddedLibrary,
                                        isLike = isLiked ?:  it.bookInfo.isLike,
                                        likes = likeCount ?: it.bookInfo.likes
                                    ),
                                )
                            } else it
                        }

                    updatedList[i] = itemStory.copy(
                        storyList = storyList
                    )
                }
                is SuggestedBooksModel -> {
                    val itemSuggestedBooks = updatedList[i] as SuggestedBooksModel
                    val bookList = itemSuggestedBooks.booksList.map {
                        if (bookId == it.id) {
                            it.copy(
                                isAddedLibrary = isAddedToLib?: it.isAddedLibrary,
                                bookInfo = it.bookInfo.copy(isAddedInLib = isAddedToLib?: it.isAddedLibrary,
                                    isLike = isLiked ?:  it.bookInfo.isLike,
                                    likes = likeCount ?: it.bookInfo.likes
                                )
                            )
                        } else it
                    }
                    updatedList[i] = itemSuggestedBooks.copy(
                        booksList = bookList
                    )
                }
                is BestsellersModel -> {
                    val itemBooks = updatedList[i] as BestsellersModel
                    val bookList = itemBooks.bestsellersList.map {
                        if (bookId == it.id) {
                            it.copy(
                                bookInfo = it.bookInfo.copy(isAddedInLib = isAddedToLib?: it.bookInfo.isAddedInLib,
                                    isLike = isLiked ?:  it.bookInfo.isLike,
                                    likes = likeCount ?: it.bookInfo.likes
                                )
                            )
                        } else it
                    }
                    updatedList[i] = itemBooks.copy(
                        bestsellersList = bookList
                    )
                }
            }
        }
        baseExploreDataList = updatedList
    }
}