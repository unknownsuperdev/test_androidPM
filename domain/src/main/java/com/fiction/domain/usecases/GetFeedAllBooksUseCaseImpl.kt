package com.fiction.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.model.SuggestedBookParams
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.domain.repository.ExploreDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFeedAllBooksUseCaseImpl(
    private val exploreDataRepo: ExploreDataRepository,
    private val getCachingImages: GetImageFromCachingUseCase
) : FlowPagingUseCase<SuggestedBookParams, BookInfoAdapterModel>() {

    override suspend fun execute(params: SuggestedBookParams): Flow<PagingData<BookInfoAdapterModel>> {
        return when (params.feedTypes) {
            FeedTypes.FOR_YOU -> {
                exploreDataRepo.getAllForYouBooks().flow.map { pagingData ->
                    pagingData.map {
                        BookInfoAdapterModel.from(
                            it,
                            getCachingImages(it.covers?.imgCover),
                            getCachingImages(it.covers?.imgSummary),
                            getCachingImages(it.author?.avatar)
                        )
                    }
                }
            }
            FeedTypes.POPULAR, FeedTypes.MOST_POPULAR -> {
                exploreDataRepo.getAllPopularBooks().flow.map { pagingData ->
                    pagingData.map {
                        BookInfoAdapterModel.from(
                            it,
                            getCachingImages(it.covers?.imgCover),
                            getCachingImages(it.covers?.imgSummary),
                            getCachingImages(it.author?.avatar)
                        )
                    }
                }
            }
            FeedTypes.NEW_ARRIVAL -> {
                exploreDataRepo.getAllNewBooks().flow.map { pagingData ->
                    pagingData.map {
                        BookInfoAdapterModel.from(
                            it,
                            getCachingImages(it.covers?.imgCover),
                            getCachingImages(it.covers?.imgSummary),
                            getCachingImages(it.author?.avatar)
                        )
                    }
                }
            }
            else -> {
                exploreDataRepo.getHotBooks(params.genreId).flow.map { pagingData ->
                    pagingData.map {
                        BookInfoAdapterModel.from(
                            it,
                            getCachingImages(it.covers?.imgCover),
                            getCachingImages(it.covers?.imgSummary),
                            getCachingImages(it.author?.avatar)
                        )
                    }
                }
            }
        }
    }
}