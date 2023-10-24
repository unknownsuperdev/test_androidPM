package com.fiction.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.AllCurrentReadBooksDataModel
import com.fiction.domain.model.OpenedAllBooks
import com.fiction.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllBooksLibraryUseCaseImpl(
    private val libraryRepository: LibraryRepository,
    private val getCachingImages: GetImageFromCachingUseCase
) : FlowPagingUseCase<OpenedAllBooks, AllCurrentReadBooksDataModel>() {

    override suspend fun execute(params: OpenedAllBooks): Flow<PagingData<AllCurrentReadBooksDataModel>> {
        return when (params) {
            OpenedAllBooks.FROM_ADDED_TO_LIBRARY -> libraryRepository.getBooksLibrary().flow.map { pagingData ->
                pagingData.map {
                    AllCurrentReadBooksDataModel.from(
                        it,
                        getCachingImages.invoke(it.covers?.imgCover),
                        getCachingImages(it.covers?.imgSummary),
                        getCachingImages(it.author?.avatar)
                    )
                }
            }
            else -> libraryRepository.getCurrentReadingBooks().flow.map { pagingData ->
                pagingData.map {
                    AllCurrentReadBooksDataModel.from(
                        it,
                        getCachingImages.invoke(it.covers?.imgCover),
                        getCachingImages(it.covers?.imgSummary),
                        getCachingImages(it.author?.avatar)
                    )
                }
            }
        }
    }
}