package com.fiction.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.AllCurrentReadBooksDataModel
import com.fiction.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllFinishedReadBooksUseCaseImpl(
    private val libraryRepository: LibraryRepository,
    private val getCachingImages: GetImageFromCachingUseCase
) : FlowPagingUseCase<Unit, AllCurrentReadBooksDataModel>() {

    override suspend fun execute(params: Unit): Flow<PagingData<AllCurrentReadBooksDataModel>> {
        return libraryRepository.getFinishedReadBooks().flow.map { pagingData ->
            pagingData.map {
                AllCurrentReadBooksDataModel.from(
                    it,
                    getCachingImages(it.covers?.imgCover),
                    getCachingImages(it.covers?.imgSummary),
                    getCachingImages(it.author?.avatar)
                )
            }
        }
    }
}
