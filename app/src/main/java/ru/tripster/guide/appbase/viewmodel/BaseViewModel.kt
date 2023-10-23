package ru.tripster.guide.appbase.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.*
import androidx.paging.PagingConfig
import ru.tripster.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.tripster.guide.App
import java.io.*

abstract class BaseViewModel : ViewModel() {

	private val _showNetworkError: MutableSharedFlow<() -> Unit> by lazy { MutableSharedFlow() }
	val showNetworkError = _showNetworkError.asSharedFlow()

	private val _showDefaultError = MutableSharedFlow<String?>()
	val showDefaultError = _showDefaultError.asSharedFlow()

	fun <T> callData(
		result: ActionResult<T>, callback: () -> Unit = {}
	): ActionResult<T> {
		return when (result) {
			is ActionResult.Success -> {
				result
			}
			is ActionResult.Error -> {
				when (result.errors) {

					is IOException -> {
						viewModelScope.launch {
							_showNetworkError.emit {
								callback()
							}
						}
					}
					else -> {
						viewModelScope.launch {
							_showDefaultError.emit(null)
						}
					}
				}

				ActionResult.Error(result.errors)
			}
		}
	}

	fun getBasePagingConfig(
		pageSize: Int = 10,
		prefetchDistance: Int = 20,
		initialLoadSize: Int = 30
	) = PagingConfig(
		pageSize = pageSize,
		prefetchDistance = prefetchDistance,
		initialLoadSize = initialLoadSize
	)

	fun getString(@StringRes strId: Int, vararg fmtArgs: Any?) = App.appContext.getString(strId, *fmtArgs)
}