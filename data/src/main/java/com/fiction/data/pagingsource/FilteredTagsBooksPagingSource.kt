package com.fiction.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.TagsService
import com.fiction.data.dataservice.sqlservice.ImagesDao
import com.fiction.data.util.Constants.Companion.NETWORK_ERROR
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.utils.Constants
import com.fiction.domain.utils.NetworkStatus
import com.fiction.domain.model.FilteredTagsIds
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.roommodels.ImagesEntity

class FilteredTagsBooksPagingSource(
    private val service: TagsService,
    private val tagsIds: FilteredTagsIds,
    private val imagesDao: ImagesDao
) : PagingSource<Int, BookItemResponse>() {

    private val STARTING_PAGE_INDEX = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookItemResponse> {
        val page = params.key ?: STARTING_PAGE_INDEX
        if (NetworkStatus.isNetworkConnected) {
            return when (val data =
                makeApiCall {
                    analyzeResponse(
                        service.getFilteredTagsBooks(tagsIds = tagsIds, page = page)
                    )
                }) {
                is ActionResult.Success -> {
                    val result = data.result.data?.items ?: emptyList()
                    cachingImages(result)
                    LoadResult.Page(
                        data = result,
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = if (result.isEmpty()) null else page + 1
                    )
                }
                is ActionResult.Error -> {
                    LoadResult.Error(data.errors)
                }
            }
        } else return LoadResult.Error(Error(NETWORK_ERROR))
    }

    override fun getRefreshKey(state: PagingState<Int, BookItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun cachingImages(result: List<BookItemResponse>) {
        result.forEach {
            val imgResponse = it.covers?.imgCover
            val key = imgResponse?.substringBefore(Constants.IMAGE_SPLIT_WORD)
            val imgSummary = key?.let {
                imagesDao.getImage(key)
            }
            if (imgSummary == null) {
                if (!imgResponse.isNullOrEmpty() && !key.isNullOrEmpty())
                    imagesDao.insertImage(ImagesEntity(key, imgResponse))
            }
        }
    }

    class Factory {
        fun create(service: TagsService, tagsIds: FilteredTagsIds, imagesDao: ImagesDao) =
            FilteredTagsBooksPagingSource(service, tagsIds, imagesDao)
    }
}