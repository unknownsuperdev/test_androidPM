package com.fiction.data.dataservice.apiservice

import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse
import retrofit2.Response
import retrofit2.http.GET

interface FinishedReadBooksService {
    @GET("/v1/library/books/finished")
    suspend fun getFinishedBooks(): Response<BaseResultModel<List<BookItemResponse>>>
}