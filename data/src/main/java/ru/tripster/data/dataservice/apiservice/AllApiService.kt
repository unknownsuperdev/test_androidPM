package ru.tripster.data.dataservice.apiservice

import ru.tripster.entities.response.*
import retrofit2.Response
import retrofit2.http.GET

interface AllApiService {

    @GET("get_memes")
    suspend fun getTestData(): Response<BaseResultModel<BaseDataModel>>

}
