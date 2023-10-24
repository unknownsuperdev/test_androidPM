package com.fiction.data.dataservice.apiservice

import com.fiction.data.util.Constants
import com.fiction.domain.model.FilteredTagsIds
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.AllTagsItemResponse
import com.fiction.entities.response.explore.BooksResponse
import com.fiction.entities.response.explore.TagBooksResponse
import com.fiction.entities.response.explore.TagResponse
import retrofit2.Response
import retrofit2.http.*

interface TagsService {

    @GET("/v1/books/tags")
    suspend fun getAllTags(): Response<BaseResultModel<List<AllTagsItemResponse>>>

    @GET("/v1/books/tags/{tag_id}")
    suspend fun getTagBooksById(@Path("tag_id") tagId: Long): Response<BaseResultModel<TagBooksResponse>>

    @GET("/v1/books/tags/popular")
    suspend fun getPopularTags(): Response<BaseResultModel<List<TagResponse>>>

    @POST("/v1/books/tags/filter")
    suspend fun getFilteredTagsBooks(
        @Body tagsIds: FilteredTagsIds,
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>
}