package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.GetSearchedMainLibBooksUseCase
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.domain.repository.LibraryRepository
import kotlinx.coroutines.withContext

class GetSearchedMainLibBooksUseCaseImpl(
    private val libraryRepository: LibraryRepository,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetSearchedMainLibBooksUseCase {

    private var mainLibBooks: List<BooksWithReadProgressBookData>? = null

    override suspend fun invoke(
        word: String
    ): List<BooksWithReadProgressBookData> {
        return withContext(dispatcher.io) {
            if (mainLibBooks.isNullOrEmpty()) {
                getFinishedBooksData()
            }
            val filterWord = word.trim().lowercase()
            return@withContext mainLibBooks?.filter {
                it.title.lowercase().contains(filterWord)
            } ?: emptyList()
        }
    }

    private suspend fun getFinishedBooksData() {
        ActionResult.Success(mainLibBooks)
     /*   when (val apiData = libraryRepository.getCurrentReadingBooks()) {
            is ActionResult.Success -> {
                apiData.result.data?.let { response ->
                    mainLibBooks = response.map { BooksWithReadProgressBookData.from(it) }
                    ActionResult.Success(mainLibBooks)
                } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
            }
            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }*/
    }
}