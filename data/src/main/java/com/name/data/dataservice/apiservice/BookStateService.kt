package com.name.data.dataservice.apiservice

import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.BookTemp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BookStateService {

    @POST("api/v1/library/add-book")
    suspend fun addBookToLibrary(@Body bookId: BookTemp): Response<BaseResultModel<String>>

    @POST("api/v1/library/remove-book")
    suspend fun removeBookToLibrary(@Body bookId: BookTemp): Response<BaseResultModel<String>>
}