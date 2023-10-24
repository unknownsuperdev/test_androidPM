package com.fiction.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.model.FilteredTagsIds
import com.fiction.domain.repository.TagsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFilteredTagsBooksUseCaseImpl(
    private val tagsRepo: TagsRepo,
    private val getCachingImages: GetImageFromCachingUseCase
) : FlowPagingUseCase<List<Long>, BookInfoAdapterModel>() {

    override suspend fun execute(params: List<Long>): Flow<PagingData<BookInfoAdapterModel>> {
        return tagsRepo.getFilteredTagsBooks(FilteredTagsIds(params)).flow.map { pagingData ->
            pagingData.map {
                BookInfoAdapterModel.from(
                    it, getCachingImages(it.covers?.imgCover),
                    getCachingImages(it.covers?.imgSummary),
                    getCachingImages(it.author?.avatar)
                )
            }
        }
    }
}