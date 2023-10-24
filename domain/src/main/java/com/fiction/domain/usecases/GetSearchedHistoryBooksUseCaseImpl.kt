package com.fiction.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.repository.SearchBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSearchedHistoryBooksUseCaseImpl(
    private val searchBookRepository: SearchBookRepository,
    private val getCachingImages: GetImageFromCachingUseCase
) : FlowPagingUseCase<String, BookInfoAdapterModel>() {

    override suspend fun execute(word: String): Flow<PagingData<BookInfoAdapterModel>> {
        return searchBookRepository.getSearchBrowsingHistoryBooks(word).flow.map { pagingData ->
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