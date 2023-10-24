package com.fiction.data.dataservice.apiservice

import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BalanceResponse
import com.fiction.entities.response.explore.coinshop.AvailableTariffsResponse
import com.fiction.entities.response.explore.coinshop.TariffReceiptData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CoinShopService {
    @GET("/v1/coinshop/balance")
    suspend fun getBalance(): Response<BaseResultModel<BalanceResponse>>

    @GET("/v1/coinshop/products")
    suspend fun getAvailableTariffs(): Response<BaseResultModel<AvailableTariffsResponse>>

    @POST("/v1/coinshop/chapters/{chapter_id}/unlock")
    suspend fun buyChapter(@Path("chapter_id") chapterId: Long): Response<BaseResultModel<BalanceResponse>>

    @POST("/v1/coinshop/purchase")
    suspend fun buyTariff(
        @Body receiptData: TariffReceiptData
    ): Response<BaseResultModel<BalanceResponse>>


    // Await new end-points
    //    @GET("/v1/coinshop/book/{book_id}/modal/unlock")
//    suspend fun getModal(@Path("book_id") bookId:Int): Response<BaseResultModel<>>
//
//    @GET("/v1/coinshop/chapters/{chapter_id}")
//    suspend fun getSpecialOffers(@Path("chapter_id") chapterId: Int): Response<BaseResultModel<>>
}