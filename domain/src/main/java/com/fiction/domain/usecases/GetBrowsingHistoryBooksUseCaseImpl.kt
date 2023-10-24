package com.fiction.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBrowsingHistoryBooksUseCaseImpl(
    private val libraryRepository: LibraryRepository,
    private val getCachingImages: GetImageFromCachingUseCase
) : FlowPagingUseCase<Unit, BookInfoAdapterModel>() {

    override suspend fun execute(params: Unit): Flow<PagingData<BookInfoAdapterModel>> {
        return libraryRepository.getBrowsingHistoryBooks().flow.map { pagingData ->
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
}