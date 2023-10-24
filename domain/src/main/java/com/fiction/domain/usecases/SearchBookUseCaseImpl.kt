package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants.Companion.ERROR_NULL_DATA
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.SearchBookUseCase
import com.fiction.domain.model.SearchBookWithName
import com.fiction.domain.model.SearchBookWithTag
import com.fiction.domain.model.SearchMainItem
import com.fiction.domain.repository.SearchBookRepository
import kotlinx.coroutines.withContext

class SearchBookUseCaseImpl(
    private val repository: SearchBookRepository,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : SearchBookUseCase {

    override suspend fun invoke(word: String): ActionResult<List<SearchMainItem>> {
        val baseSearchBookDataModel = mutableListOf<SearchMainItem>()

        return withContext(dispatcher.io) {
            when (val apiData = repository.getSearchBookList(word)) {
                is ActionResult.Success -> {

                    apiData.result.data?.let { response ->
                        response.searchBookWithName?.let { itemResponseList ->
                            val imgValue = itemResponseList.map { it.covers?.imgCover }
                            cachingImages.invoke(imgValue)
                            baseSearchBookDataModel.addAll(itemResponseList.map {
                                SearchBookWithName.from(
                                    it,
                                    getCachingImages(it.covers?.imgCover),
                                    getCachingImages(it.covers?.imgSummary),
                                    getCachingImages(it.author?.avatar)
                                )
                            })
                        }
                        response.searchBookWithTag?.let { itemResponseList ->
                            itemResponseList.forEach {
                                baseSearchBookDataModel.add(SearchBookWithTag.from(it))
                            }
                        }
                        ActionResult.Success(baseSearchBookDataModel)
                    } ?: ActionResult.Error(CallException(ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
    }
}
