package com.core.network.rest.repository

import com.core.network.rest.response.ResponseFactory
import com.core.network.rest.response.model.ApiResponse
import com.core.network.rest.response.model.ApiResponseError
import com.core.network.rest.response.model.ApiResponseSuccess
import com.core.network.rest.response.model.BaseResponseData
import kotlinx.coroutines.Deferred

suspend fun <I : Any, O : Any> convertToDomainWrapBaseResponse(
    deferred: BaseResponseData<I>,
    mapper: (I) -> O
): ApiResponse<O> {
    return when (val response = ResponseFactory.createResponse(deferred)) {
        is ApiResponseSuccess -> ApiResponseSuccess(mapper.invoke(response.data))
        is ApiResponseError -> ApiResponseError(response.code, response.errors)
    }
}

suspend fun <I : Any, O : Any> convertToDomainWrapBaseResponse(
    deferred: BaseResponseData<I>,
    header: String,
    mapper: (I) -> O
): ApiResponse<O> {
    return when (val response = ResponseFactory.createResponse(deferred, header)) {
        is ApiResponseSuccess -> {
            ApiResponseSuccess(mapper.invoke(response.data))
        }
        is ApiResponseError -> {
            ApiResponseError(response.code, response.errors, response.header)
        }
    }
}

suspend fun <I : Any, O : Any> convertToDomain(
    deferred: I,
    header: String,
    mapper: (I) -> O
): ApiResponse<O> {
    return when (val response = ResponseFactory.createResponse(deferred, header)) {
        is ApiResponseSuccess -> ApiResponseSuccess(mapper.invoke(response.data))
        is ApiResponseError -> ApiResponseError(response.code, response.errors, response.header)
    }
}

suspend fun <I : Any, O : Any> convertToDomain(
    deferred: I,
    mapper: (I) -> O
): ApiResponse<O> {
    return when (val response = ResponseFactory.createResponse(deferred)) {
        is ApiResponseSuccess -> ApiResponseSuccess(mapper.invoke(response.data))
        is ApiResponseError -> ApiResponseError(response.code, response.errors, response.header)
    }
}

