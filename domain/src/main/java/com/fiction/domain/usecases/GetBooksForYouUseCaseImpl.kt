package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.core.utils.Encryption
import com.fiction.core.utils.Uuid
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetBooksForYouUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.SetImageCacheUseCase
import com.fiction.domain.model.onboarding.BooksForYou
import com.fiction.domain.repository.OnBoardingRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetBooksForYouUseCaseImpl(
    private val onBoardingRepo: OnBoardingRepo,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val setImageCacheUseCase: SetImageCacheUseCase,
    private val dispatcher: CoroutineDispatcherProvider,
    private val uuid: Uuid
) : GetBooksForYouUseCase {
    var books: List<BooksForYou> = listOf()

    override suspend fun invoke(isClear: Boolean): ActionResult<List<BooksForYou>> =
        withContext(dispatcher.io) {
            if (isClear) {
                books = listOf()
                return@withContext ActionResult.Success(books)
            }
            if (books.isNotEmpty()) ActionResult.Success(books)
            else
                when (val apiData = onBoardingRepo.getBooksForYou()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let { response ->
                            val imgValue = response.map { it.covers?.imgSummary }
                            cachingImages.invoke(imgValue)

                            val booksForYou = response.map {
                                val decryptText = it.firstChapter?.let { chapter ->
                                    with(chapter) {
                                        Encryption.decryptText(
                                            uuid.getUuid(),
                                            title,
                                            text,
                                            id
                                        )
                                    }
                                }

                                val firstChapter = it.firstChapter?.copy(text = decryptText)

                                BooksForYou.from(
                                    it.copy(firstChapter = firstChapter),
                                    getCachingImages(it.covers?.imgCover),
                                    getCachingImages(it.covers?.imgSummary),
                                    getCachingImages(it.author?.avatar)
                                )

                            }
                            books = booksForYou

                            booksForYou.forEach {
                                setImageCacheUseCase(it.cover)
                            }
                            ActionResult.Success(booksForYou)
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }
}
