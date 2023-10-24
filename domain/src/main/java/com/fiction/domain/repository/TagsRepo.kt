package com.fiction.domain.repository

import androidx.paging.Pager
import com.fiction.core.ActionResult
import com.fiction.domain.model.FilteredTagsIds
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.AllTagsItemResponse
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.TagBooksResponse
import com.fiction.entities.response.explore.TagResponse

interface TagsRepo {

    suspend fun getAllTags(): ActionResult<BaseResultModel<List<AllTagsItemResponse>>>

    suspend fun getTagBooksById(tagId: Long): ActionResult<BaseResultModel<TagBooksResponse>>

    suspend fun getPopularTags(): ActionResult<BaseResultModel<List<TagResponse>>>

    suspend fun getFilteredTagsBooks(tagsIds: FilteredTagsIds): Pager<Int, BookItemResponse>

}