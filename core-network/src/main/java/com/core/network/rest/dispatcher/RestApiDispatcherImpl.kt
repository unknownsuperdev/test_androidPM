@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.core.network.rest.dispatcher

import com.core.network.rest.ApplicationErrors
import com.core.network.rest.RestProvider
import com.core.network.rest.header.Header
import com.core.network.rest.response.model.ApiResponse
import com.core.network.rest.response.model.ApiResponseError
import com.core.network.rest.response.model.BaseResponseData
import com.core.network.rest.response.model.ErrorResponseData
import com.core.network.rest.utils.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.component.inject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.reflect.KClass

@OptIn(ObsoleteCoroutinesApi::class)
internal class RestApiDispatcherImpl<REST : Any>(
    kClass: KClass<REST>,
    domains: MutableList<String>,
    headers: List<Header>,
    scalarResponse: Boolean
) : RestApiDispatcher<REST>(kClass, domains, headers, scalarResponse) {

    private val networkUtils: NetworkUtils by inject()
    private val retrofitProvider: RestProvider by inject()
    private var rest = retrofitProvider.create(domains.first(), kClass, headers, scalarResponse)

    private var tryCountConnect = 0

    override suspend fun <T> executeRequest(header: String?, block: suspend (restApi: REST) -> ApiResponse<T>): ApiResponse<T> {
        return if (networkUtils.isInternetAvailable()) {
            dgaRequestWrapper(block, header)
        } else {
            ApiResponseError(
                ApplicationErrors.NETWORK_ERROR,
                listOf(ErrorResponseData(null, null, 0, ApplicationErrors.NETWORK_ERROR))
            )
        }
    }

    private suspend fun <T> dgaRequestWrapper(req: suspend (restApi: REST) -> ApiResponse<T>, header: String?,): ApiResponse<T> {
        val result: ApiResponse<T> = try {
            val api = rest
            val response = req.invoke(api)
            response
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

        return if (isUndefinedResult(result) && networkUtils.isInternetAvailable() && domains.isNotEmpty()) {
            if (tryCountConnect++ > 1) {
                tryCountConnect = 0
                domainDeprecated()
            }

            dgaRequestWrapper(req, header)
        } else {
            tryCountConnect = 0
            result
        }
    }

    private fun domainDeprecated() {
        if (domains.isNotEmpty()) {
            domains.removeAt(0)
        }
    }

    private fun isUndefinedResult(response: ApiResponse<*>): Boolean {
        if (response is ApiResponseError && response.errors.isNotEmpty()) {
            if (response.errors[0].code == ApplicationErrors.TIMEOUT || response.errors[0].code == ApplicationErrors.UNKNOW_HOST) {
                return true
            }
        }
        return false
    }

    override suspend fun setDomain(domain: List<String>) {
        domains = domain.toMutableList()

        rest = retrofitProvider.create(domains.first(), kClass, headers, scalarResponse)
    }

    private fun <T> tryParseErrors(e: HttpException, header: String? = null): ApiResponseError<T>? {
        val errorResponse = e.response()
        var headerField: String? = null
        header?.let {
            headerField = errorResponse?.headers()?.get(header)
        }
        return e.response()?.errorBody()?.bytes()?.let {
            val response =
                Gson().fromJson(String(it), BaseResponseData::class.java)
            ApiResponseError(e.code(), response.errors, headerField)
        }
    }

    override  fun getDomain() = domains.first()

}