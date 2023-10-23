package ru.tripster.data.dataservice.pagingSource.chat

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.chat.ChatApiService
import ru.tripster.data.util.analyzeResponse
import java.io.IOException
import ru.tripster.entities.response.chat.ResultResponse


class OrderCommentsPagingSource(
    private val service: ChatApiService,
    private val orderId: Int
) : PagingSource<Int, ResultResponse>() {
    private val STARTING_PAGE_INDEX = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultResponse> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getOrderComments(orderId, page, 30)
            when (val data = analyzeResponse(response)) {
                is ActionResult.Success -> {
                    LoadResult.Page(
                        data = data.result.results ?: emptyList(),
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = page + 1
                    )
                }
                is ActionResult.Error -> {
                    LoadResult.Error(data.errors)
                }
            }
        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */

    override fun getRefreshKey(state: PagingState<Int, ResultResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    class Factory {
        fun create(service: ChatApiService, orderId: Int) =
            OrderCommentsPagingSource(service, orderId)
    }

}