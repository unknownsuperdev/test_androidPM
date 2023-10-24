package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.GetTagBooksByIdUseCase
import com.fiction.domain.model.BookInfoAdapterModelList
import com.fiction.domain.repository.TagsRepo
import kotlinx.coroutines.withContext

class GetTagBooksByIdUseCaseImpl(
    private val tagsRepo: TagsRepo,
    private val cachingBookImagesUseCase: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetTagBooksByIdUseCase {

    override suspend fun invoke(tagId: Long): ActionResult<BookInfoAdapterModelList> =
        withContext(dispatcher.io) {
            when (val apiData = tagsRepo.getTagBooksById(tagId)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        val images = it.books?.map { it.covers?.imgCover }
                        images?.let { cachingBookImagesUseCase.invoke(it) }
                        val bookList = BookInfoAdapterModelList.from(it,getCachingImages)
                        ActionResult.Success(bookList)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}