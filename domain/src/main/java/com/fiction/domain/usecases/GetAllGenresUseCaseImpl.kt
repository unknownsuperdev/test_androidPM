package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetAllGenresUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.GenreDataModel
import com.fiction.domain.repository.ExploreDataRepository
import kotlinx.coroutines.withContext

class GetAllGenresUseCaseImpl(
    private val exploreDataRepository: ExploreDataRepository,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetAllGenresUseCase {

    private var genreList = listOf<GenreDataModel>()
    override suspend fun invoke(): ActionResult<List<GenreDataModel>> =
        withContext(dispatcher.io) {
            if (genreList.isNotEmpty()){
                ActionResult.Success(genreList)
            }else{
                when (val apiData = exploreDataRepository.getAllGenres()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let { response ->
                            val imgValue = response.map { it.icon }
                            cachingImages.invoke(imgValue)
                            val genres = response.map { GenreDataModel.from(it, getCachingImages(it.icon))}
                            genreList = genres
                            ActionResult.Success(genres)
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
            }
        }
}