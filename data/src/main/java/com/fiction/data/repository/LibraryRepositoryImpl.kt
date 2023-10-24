package com.fiction.data.repository

import androidx.paging.Pager
import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.LibraryService
import com.fiction.data.dataservice.sqlservice.ImagesDao
import com.fiction.data.pagingsource.BooksSection
import com.fiction.data.pagingsource.LibraryBooksPagingSource
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.getPagerConfig
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.LibraryRepository
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse

class LibraryRepositoryImpl(
    private val libraryService: LibraryService,
    private val imagesDao: ImagesDao,
) : LibraryRepository {

    override suspend fun getBooksLibrary(pageSize: Int): Pager<Int, BookItemResponse> =
        Pager(getPagerConfig(pageSize)) {
            LibraryBooksPagingSource.Factory()
                .create(
                    libraryService,
                    BooksSection.ADDED_LIBRARY,
                    pageSize = pageSize,
                    imagesDao = imagesDao
                )
        }

    override suspend fun getBrowsingHistoryBooks(): Pager<Int, BookItemResponse> =
        Pager(getPagerConfig()) {
            LibraryBooksPagingSource.Factory()
                .create(libraryService, BooksSection.BROWSING_HISTORY, imagesDao = imagesDao)
        }

    override suspend fun getCurrentReadingBooks(pageSize: Int): Pager<Int, BookItemResponse> =
        Pager(getPagerConfig(pageSize)) {
            LibraryBooksPagingSource.Factory()
                .create(
                    libraryService,
                    BooksSection.CURRENT_READING,
                    pageSize = pageSize,
                    imagesDao = imagesDao
                )
        }

    override suspend fun getFinishedReadBooks(pageSize: Int): Pager<Int, BookItemResponse> =
        Pager(getPagerConfig(pageSize)) {
            LibraryBooksPagingSource.Factory()
                .create(
                    libraryService,
                    BooksSection.FINISHED,
                    pageSize = pageSize,
                    imagesDao = imagesDao
                )
        }

    override suspend fun getLibAlsoLikeBooks(): ActionResult<BaseResultModel<List<BookItemResponse>>> =
        makeApiCall {
            analyzeResponse(libraryService.getLibAlsoLikeBooks())
        }
}