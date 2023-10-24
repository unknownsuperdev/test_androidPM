package com.fiction.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.ExploreService
import com.fiction.data.dataservice.sqlservice.ImagesDao
import com.fiction.data.pagingsource.BooksByGenrePagingSource
import com.fiction.data.pagingsource.BooksSection
import com.fiction.data.pagingsource.ExploreBooksPagingSource
import com.fiction.data.util.Constants.Companion.DEFAULT_PAGE_SIZE
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.ExploreDataRepository
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.*

class ExploreDataRepositoryImpl(
    private val exploreService: ExploreService,
    private val imagesDao: ImagesDao
) : ExploreDataRepository {

    override suspend fun getExploreData(): ActionResult<BaseResultModel<List<ExploreDataItemResponse>>> =
        makeApiCall {
            analyzeResponse(
                exploreService.getExploreData()
            )
        }

    override suspend fun getAllGenres(): ActionResult<BaseResultModel<List<GenreResponse>>> =
        makeApiCall {
            analyzeResponse(
                exploreService.getAllGenres()
            )
        }

    override suspend fun getGenreById(genreId: Long): Pager<Int, BookItemResponse> =
        Pager(
            PagingConfig(
                DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = DEFAULT_PAGE_SIZE,
                initialLoadSize = DEFAULT_PAGE_SIZE
            )
        ) {
            BooksByGenrePagingSource.Factory()
                .create(exploreService,genreId, imagesDao)
        }


    override suspend fun getAllForYouBooks(): Pager<Int, BookItemResponse> =
        Pager(
            PagingConfig(
                DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = DEFAULT_PAGE_SIZE,
                initialLoadSize = DEFAULT_PAGE_SIZE
            )
        ) {
            ExploreBooksPagingSource.Factory()
                .create(exploreService, BooksSection.FOR_YOU, imagesDao)
        }

    override suspend fun getAllPopularBooks(): Pager<Int, BookItemResponse> =
        Pager(
            PagingConfig(
                DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = DEFAULT_PAGE_SIZE,
                initialLoadSize = DEFAULT_PAGE_SIZE
            )
        ) {
            ExploreBooksPagingSource.Factory()
                .create(exploreService, BooksSection.POPULAR, imagesDao)
        }

    override suspend fun getMostPopularBooks(): ActionResult<BaseResultModel<BooksResponse>> =
        makeApiCall {
            analyzeResponse(
                exploreService.getAllPopularBooks(page = 1)
            )
        }


    override suspend fun getAllNewBooks(): Pager<Int, BookItemResponse> =
        Pager(
            PagingConfig(
                DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = DEFAULT_PAGE_SIZE,
                initialLoadSize = DEFAULT_PAGE_SIZE
            )
        ) {
            ExploreBooksPagingSource.Factory()
                .create(exploreService, BooksSection.NEW_ARRIVAL, imagesDao)
        }

    override suspend fun getHotBooks(genre_id: Int): Pager<Int, BookItemResponse> =
        Pager(
            PagingConfig(
                DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = DEFAULT_PAGE_SIZE,
                initialLoadSize = DEFAULT_PAGE_SIZE
            )
        ) {
            ExploreBooksPagingSource.Factory()
                .create(exploreService, BooksSection.HOT_ROMANCE, imagesDao, genre_id)
        }
}