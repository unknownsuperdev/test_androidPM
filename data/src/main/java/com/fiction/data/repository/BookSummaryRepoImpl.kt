package com.fiction.data.repository

import androidx.paging.Pager
import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.BookSummaryService
import com.fiction.data.dataservice.sqlservice.ImagesDao
import com.fiction.data.pagingsource.BooksSection
import com.fiction.data.pagingsource.LibraryBooksPagingSource
import com.fiction.data.pagingsource.YouMayAlsoLikeBooksPagingSource
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.getPagerConfig
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.BookSummeryRepo
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookInfoResponse
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.BooksResponse
import com.fiction.entities.response.explore.ReadProgress
import com.fiction.entities.response.reader.ResponseMessage

class BookSummaryRepoImpl(
    private val bookSummaryService: BookSummaryService,
    private val imagesDao: ImagesDao
) : BookSummeryRepo {

    override suspend fun getBookInfoById(bookId: Long) =
        makeApiCall {
            analyzeResponse(
                bookSummaryService.getBookInfoById(bookId)
            )
        }

    override suspend fun getBookChapters(bookId: Long): ActionResult<BaseResultModel<BookInfoResponse>> =
        makeApiCall {
            analyzeResponse(
                bookSummaryService.getBookInfoById(bookId)
            )
        }

    override suspend fun getChapterInfoById(
        chapterId: Long
    ) =
        makeApiCall {
            analyzeResponse(
                bookSummaryService.getChapterInfoById(chapterId)
            )
        }

    override suspend fun setReadProgress(
        chapterId: Long,
        readProgress: ReadProgress
    ) =
        makeApiCall {
            analyzeResponse(
                bookSummaryService.setReadProgress(readProgress, chapterId)
            )
        }

    override suspend fun setLike(bookId: Long): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(
                bookSummaryService.setLike(bookId)
            )
        }

    override suspend fun removeLike(bookId: Long): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(
                bookSummaryService.removeLike(bookId)
            )
        }

    override suspend fun getSuggestionBooks(bookId: Long): ActionResult<BaseResultModel<List<BookItemResponse>>> =
        makeApiCall {
            analyzeResponse(
                bookSummaryService.getSuggestionBooks(bookId)
            )
        }

    override suspend fun getAlsoLikeBooks(bookId: Long): ActionResult<BaseResultModel<BooksResponse>> =
        makeApiCall {
            analyzeResponse(bookSummaryService.getAlsoLikeBooks(bookId))
        }

    override suspend fun getAlsoLikeBooksWithPaging(
        bookId: Long,
        pageSize: Int
    ): Pager<Int, BookItemResponse> =
        Pager(getPagerConfig(pageSize)) {
            YouMayAlsoLikeBooksPagingSource.Factory()
                .create(
                    bookSummaryService,
                    pageSize = pageSize,
                    bookId,
                    imagesDao = imagesDao
                )
        }

}