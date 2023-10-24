package com.fiction.data.dataservice.apiservice

import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookTemp
import com.fiction.entities.response.reader.ResponseMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BookStateService {

    @POST("/v1/library/books/add")
    suspend fun addBookToLibrary(@Body bookId: BookTemp): Response<BaseResultModel<ResponseMessage>>

    @POST("/v1/library/books/remove")
    suspend fun removeBookToLibrary(@Body bookId: BookTemp): Response<BaseResultModel<ResponseMessage>>
}