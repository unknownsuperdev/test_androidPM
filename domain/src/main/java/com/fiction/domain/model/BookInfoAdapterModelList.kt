package com.fiction.domain.model

import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.entities.response.explore.AllGenresItemResponse
import com.fiction.entities.response.explore.TagBooksResponse

data class BookInfoAdapterModelList(
    val title: String,
    val books: List<BookInfoAdapterModel>
) {

    companion object {
        suspend fun from(
            data: AllGenresItemResponse,
            getCachingImages: GetImageFromCachingUseCase
        ) =
            BookInfoAdapterModelList(
                data.name ?: "",
                data.books?.map {
                    val imgCover = getCachingImages(it.covers?.imgCover)
                    BookInfoAdapterModel.from(
                        it,
                        imgCover,
                        getCachingImages(it.covers?.imgSummary),
                        getCachingImages(it.author?.avatar)
                    )
                } ?: listOf()
            )

        suspend fun from(
            data: TagBooksResponse,
            getCachingImages: GetImageFromCachingUseCase
        ) =
            BookInfoAdapterModelList(
                data.name ?: "",
                data.books?.map {
                    BookInfoAdapterModel.from(
                        it,
                        getCachingImages(it.covers?.imgCover),
                        getCachingImages(it.covers?.imgSummary),
                        getCachingImages(it.author?.avatar)
                    )
                } ?: listOf()
            )
    }
}