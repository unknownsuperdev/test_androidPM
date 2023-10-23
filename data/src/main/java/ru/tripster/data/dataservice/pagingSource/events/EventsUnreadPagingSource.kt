package ru.tripster.data.dataservice.pagingSource.events

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.events.EventsApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.entities.response.events.ResultModel
import java.io.IOException

class EventsUnreadPagingSource(
    private val service: EventsApiService
) : PagingSource<Int, ResultModel>() {
    private val STARTING_PAGE_INDEX = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultModel> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getEventsUnread(0, page = page)
            when (val data = analyzeResponse(response)) {
                is ActionResult.Success -> {
                    LoadResult.Page(
                        data = data.result.results ?: emptyList(),
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = page + 1
                    )
                }
                is ActionResult.Error -> {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = null
                    )
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

    override fun getRefreshKey(state: PagingState<Int, ResultModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    class Factory {
        fun create(service: EventsApiService) =
            EventsUnreadPagingSource(service)
    }
}