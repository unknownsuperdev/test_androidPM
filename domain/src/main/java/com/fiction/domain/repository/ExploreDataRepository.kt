package com.fiction.domain.repository

import androidx.paging.Pager
import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.*


interface ExploreDataRepository {

    suspend fun getExploreData(): ActionResult<BaseResultModel<List<ExploreDataItemResponse>>>

    suspend fun getAllGenres(): ActionResult<BaseResultModel<List<GenreResponse>>>

    suspend fun getGenreById(genreId: Long):  Pager<Int, BookItemResponse>

    suspend fun getAllForYouBooks(): Pager<Int, BookItemResponse>

    suspend fun getAllPopularBooks(): Pager<Int, BookItemResponse>

    suspend fun getMostPopularBooks(): ActionResult<BaseResultModel<BooksResponse>>

    suspend fun getAllNewBooks(): Pager<Int, BookItemResponse>

    suspend fun getHotBooks(genre_id: Int): Pager<Int, BookItemResponse>
}