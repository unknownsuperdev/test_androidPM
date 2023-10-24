package com.fiction.domain.repository

import androidx.paging.Pager
import com.fiction.core.ActionResult
import com.fiction.domain.utils.Constants.Companion.DEFAULT_PAGE_SIZE
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.*
import com.fiction.entities.response.reader.ResponseMessage

interface BookSummeryRepo {

    suspend fun getBookInfoById(bookId: Long): ActionResult<BaseResultModel<BookInfoResponse>>

    suspend fun getBookChapters(bookId: Long): ActionResult<BaseResultModel<BookInfoResponse>>

    suspend fun getChapterInfoById(
        chapterId: Long
    ): ActionResult<BaseResultModel<ChapterInfoResponse>>

    suspend fun setReadProgress(
        chapterId: Long,
        readProgress: ReadProgress
    ): ActionResult<BaseResultModel<Any>>

    suspend fun setLike(bookId: Long): ActionResult<BaseResultModel<ResponseMessage>>

    suspend fun removeLike(bookId: Long): ActionResult<BaseResultModel<ResponseMessage>>

    suspend fun getSuggestionBooks(bookId: Long): ActionResult<BaseResultModel<List<BookItemResponse>>>

    suspend fun getAlsoLikeBooks(bookId: Long): ActionResult<BaseResultModel<BooksResponse>>

    suspend fun getAlsoLikeBooksWithPaging(bookId: Long, pageSize: Int = DEFAULT_PAGE_SIZE): Pager<Int, BookItemResponse>
}