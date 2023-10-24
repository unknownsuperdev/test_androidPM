package com.fiction.data.util


import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.data.util.Constants.Companion.NETWORK_ERROR
import com.fiction.data.util.Constants.Companion.NETWORK_LOST
import com.fiction.entities.response.ErrorApplicationVersionResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

suspend fun <R> makeApiCall(call: suspend () -> ActionResult<R>) = try {
    call()
} catch (e: Exception) {
    when (e) {
        is UnknownHostException -> {
            ActionResult.Error(CallException(NETWORK_LOST, listOf(NETWORK_ERROR)))
        }
        is IOException -> {
            ActionResult.Error(CallException(NETWORK_LOST, listOf(NETWORK_ERROR)))
        }
        else -> {
            ActionResult.Error(e)
        }
    }
}

fun <Model> analyzeResponse(
    response: Response<Model>
): ActionResult<Model> {
    when {
        response.isSuccessful -> {
            val responseBody = response.body()

            responseBody?.let {
                return ActionResult.Success(it)
            } ?: return ActionResult.Error(
                CallException(
                    response.code(),
                    listOf(response.message())
                )
            )
        }
        else -> {
            val errorBody: ErrorApplicationVersionResponse =
                Gson().fromJson(
                    response.errorBody()?.string().toString(),
                    ErrorApplicationVersionResponse::class.java
                )
            val message = mutableListOf<String>()
            if (!errorBody.errors.isNullOrEmpty()) {
                /*   if (!errorBody.errors!![0].messages.isNullOrEmpty())
                       errorBody.errors!![0].messages else listOf()*/
                errorBody.errors!!.forEach {
                    message.add("${it.name}+${it.messages?.get(0)}")
                }
            }
            return ActionResult.Error(CallException(response.code(), message))
        }
    }
}

fun <T> emitFlow(action: suspend () -> T) = flow { emit(action.invoke()) }
