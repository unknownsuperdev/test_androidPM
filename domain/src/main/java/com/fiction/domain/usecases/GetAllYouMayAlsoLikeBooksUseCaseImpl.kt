package com.fiction.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.repository.BookSummeryRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllYouMayAlsoLikeBooksUseCaseImpl(
    private val bookSummeryRepo: BookSummeryRepo,
    private val getCachingImages: GetImageFromCachingUseCase
) : FlowPagingUseCase<Long, BookInfoAdapterModel>() {

    override suspend fun execute(params: Long): Flow<PagingData<BookInfoAdapterModel>> =
        bookSummeryRepo.getAlsoLikeBooksWithPaging(params).flow.map { pagingData ->
            pagingData.map {
                BookInfoAdapterModel.from(
                    it,
                    getCachingImages.invoke(it.covers?.imgCover),
                    getCachingImages(it.covers?.imgSummary),
                    getCachingImages(it.author?.avatar)
                )
            }
        }
}