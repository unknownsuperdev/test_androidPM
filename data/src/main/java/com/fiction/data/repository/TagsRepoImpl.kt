package com.fiction.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.TagsService
import com.fiction.data.dataservice.sqlservice.ImagesDao
import com.fiction.data.pagingsource.FilteredTagsBooksPagingSource
import com.fiction.data.util.Constants
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.model.FilteredTagsIds
import com.fiction.domain.repository.TagsRepo
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.AllTagsItemResponse
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.TagBooksResponse
import com.fiction.entities.response.explore.TagResponse

class TagsRepoImpl(
    private val tagsService: TagsService,
    private val imagesDao: ImagesDao
) : TagsRepo {

    override suspend fun getAllTags(): ActionResult<BaseResultModel<List<AllTagsItemResponse>>> =
        makeApiCall {
            analyzeResponse(
                tagsService.getAllTags()
            )
        }

    override suspend fun getTagBooksById(tagId: Long): ActionResult<BaseResultModel<TagBooksResponse>> =
        makeApiCall {
            analyzeResponse(
                tagsService.getTagBooksById(tagId)
            )
        }

    override suspend fun getPopularTags(): ActionResult<BaseResultModel<List<TagResponse>>> =
        makeApiCall {
            analyzeResponse(
                tagsService.getPopularTags()
            )
        }

    override suspend fun getFilteredTagsBooks(tagsIds: FilteredTagsIds): Pager<Int, BookItemResponse> =
        Pager(
            PagingConfig(
                Constants.DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = Constants.DEFAULT_PAGE_SIZE,
                initialLoadSize = Constants.DEFAULT_PAGE_SIZE
            )
        ) {
            FilteredTagsBooksPagingSource.Factory()
                .create(tagsService, tagsIds, imagesDao)
        }
}