package ru.tripster.data.dataservice.pagingSource.calendar

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.calendar.ExperienceApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.entities.response.calendar.filtering.ExperienceTitleResponseModel
import java.io.IOException

class ExperiencePagingSource(
    private val service: ExperienceApiService,
    private val isVisible: Boolean
) : PagingSource<Int, ExperienceTitleResponseModel>() {
    private val STARTING_PAGE_INDEX = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExperienceTitleResponseModel> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getExperience(isVisible, page, 20)
            when (val data = analyzeResponse(response)) {
                is ActionResult.Success -> {
                    val result = data.result.results ?: emptyList()
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


    class Factory {
        fun create(service: ExperienceApiService, isVisible: Boolean) =
            ExperiencePagingSource(service, isVisible)
    }

    override fun getRefreshKey(state: PagingState<Int, ExperienceTitleResponseModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}