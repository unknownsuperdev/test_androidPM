package com.fiction.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.domain.repository.SearchBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSearchedBooksLibraryUseCaseImpl(
    private val searchBookRepository: SearchBookRepository,
    private val getCachingImages: GetImageFromCachingUseCase
) : FlowPagingUseCase<String, BooksWithReadProgressBookData>() {

    override suspend fun execute(word: String): Flow<PagingData<BooksWithReadProgressBookData>> {
        return searchBookRepository.getSearchLibraryBooks(word).flow.map { pagingData ->
            pagingData.map {
                BooksWithReadProgressBookData.from(
                    it,
                    getCachingImages.invoke(it.covers?.imgCover),
                    getCachingImages(it.covers?.imgSummary),
                    getCachingImages(it.author?.avatar)
                )
            }
        }
    }
}