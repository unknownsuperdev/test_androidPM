package com.core.network.rest.response

import com.core.network.rest.ApplicationErrors
import com.core.network.rest.response.model.ApiResponse
import com.core.network.rest.response.model.ApiResponseError
import com.core.network.rest.response.model.ApiResponseSuccess
import com.core.network.rest.response.model.BaseResponseData
import com.core.network.rest.response.model.ErrorResponseData
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal object ResponseFactory {

    private val TAG = this::class.java.simpleName

    suspend fun <T : Any> createResponse(deferred: BaseResponseData<T>, header: String? = null): ApiResponse<T> {
        return createResponse(deferred.data, header)
    }

    suspend fun <T : Any> createResponse(deferred: T, header: String? = null): ApiResponse<T> {
        return try {
            ApiResponseSuccess(deferred)
        } catch (httpException: HttpException) {
            try {
                tryParseErrors(httpException, header)
                    ?: ApiResponseError(httpException.code(), listOf(ErrorResponseData(code = httpException.code())))
            } catch (e: Exception) {
                ApiResponseError(httpException.code(), listOf(ErrorResponseData(code = httpException.code())))
            }

        } catch (e: UnknownHostException){
            ApiResponseError(ApplicationErrors.UNKNOW_HOST, listOf(ErrorResponseData(code = ApplicationErrors.UNKNOW_HOST)))
        } catch (e: SocketTimeoutException) {
            ApiResponseError(ApplicationErrors.TIMEOUT, listOf(ErrorResponseData(code = ApplicationErrors.TIMEOUT)))
        } catch (e: Exception) {
            ApiResponseError(ApplicationErrors.UNKNOWN, listOf(ErrorResponseData(code = ApplicationErrors.UNKNOWN)))
        }
    }

    private fun <T : Any> tryParseErrors(e: HttpException, header: String? = null): ApiResponseError<T>? {
        val errorResponse = e.response()
        var headerField: String? = null
        header?.let {
            headerField = errorResponse?.headers()?.get(header)
        }
        return e.response()?.errorBody()?.bytes()?.let {
            val response =
                Gson().fromJson<BaseResponseData<T>>(String(it), BaseResponseData::class.java)
            ApiResponseError(e.code(), response.errors, headerField)
        }
    }

}