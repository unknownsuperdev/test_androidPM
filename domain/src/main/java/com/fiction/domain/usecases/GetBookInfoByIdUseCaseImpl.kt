package com.fiction.domain.usecases

import android.util.Log
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetBookInfoByIdUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BookSummaryInfo
import com.fiction.domain.repository.BookSummeryRepo
import kotlinx.coroutines.withContext

class GetBookInfoByIdUseCaseImpl(
    private val bookSummeryRepo: BookSummeryRepo,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetBookInfoByIdUseCase {

    override suspend fun invoke(bookId: Long): ActionResult<BookSummaryInfo> =
        withContext(dispatcher.io) {
            when (val apiData = bookSummeryRepo.getBookInfoById(bookId)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        Log.i("GetBookInfoByIdUseCase", "$bookId invoke: $response")
                        val imgValue = response.book?.covers?.imgSummary
                        val avatarAuth = response.book?.author?.avatar
                        cachingImages.invoke(listOf(imgValue, avatarAuth))
                        val bookInfo =
                            BookSummaryInfo.from(response, getCachingImages.invoke(imgValue), getCachingImages.invoke(avatarAuth))
                        ActionResult.Success(bookInfo)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}