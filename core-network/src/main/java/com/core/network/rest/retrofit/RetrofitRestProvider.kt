package com.core.network.rest.retrofit

import com.core.network.rest.RestProvider
import com.core.network.rest.header.Header
import com.core.network.rest.header.HeaderInterceptor
import com.google.gson.GsonBuilder
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.reflect.KClass

internal class RetrofitRestProvider : RestProvider {

    override fun <R : Any> create(
        baseUrl: String,
        kClass: KClass<R>,
        headers: List<Header>,
        scalarResponse: Boolean
    ): R {
        return buildRetrofit(baseUrl, scalarResponse)
            .client(getOkHttpClient(headers))
            .build()
            .create(kClass.java)
    }

    override fun <R : Any> create(baseUrl: String, kClass: KClass<R>): R {
        return buildRetrofit(baseUrl, false)
            .client(getOkHttpClient())
            .build()
            .create(kClass.java)
    }

    private fun buildRetrofit(baseUrl: String, scalarResponse: Boolean): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(if (scalarResponse) ScalarsConverterFactory.create() else GsonConverterFactory.create())
            .baseUrl(baseUrl)
    }

    private fun buildOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectionSpecs(
                arrayListOf(
                    ConnectionSpec.CLEARTEXT,
                    ConnectionSpec.Builder(
                        ConnectionSpec.COMPATIBLE_TLS
                    ).build()
                )
            )
            .addInterceptor(OkHttpProfilerInterceptor())
    }

    private fun getOkHttpClient() = buildOkHttpClient().build()

    private fun getOkHttpClient(headers: List<Header>) =
        buildOkHttpClient()
            .addNetworkInterceptor(HeaderInterceptor(headers))
            .build()

}