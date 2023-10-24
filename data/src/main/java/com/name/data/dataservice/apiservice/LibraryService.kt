package com.name.data.dataservice.apiservice

import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.SuggestedBooksItemResponse
import retrofit2.Response
import retrofit2.http.GET

interface LibraryService {

    @GET("api/v1/library/get-books")
    suspend fun getBooksLibrary(): Response<BaseResultModel<List<SuggestedBooksItemResponse>>>
}
