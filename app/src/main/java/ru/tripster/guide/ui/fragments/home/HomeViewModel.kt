package ru.tripster.guide.ui.fragments.home

import androidx.lifecycle.viewModelScope
import ru.tripster.core.*
import ru.tripster.domain.interactors.*
import ru.tripster.domain.model.*
import ru.tripster.guide.appbase.viewmodel.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val testUseCase: TestUseCase) : BaseViewModel() {

    private val _list: MutableStateFlow<List<DataInfo>?> by lazy { MutableStateFlow(null) }
    val list= _list.asSharedFlow()

    private val _goToFragment: MutableSharedFlow<String> by lazy { MutableSharedFlow() }
    val goToFragment = _goToFragment.asSharedFlow()

    fun setValue() {
        viewModelScope.launch {
            _goToFragment.emit("Hello")
        }
    }

    init {
        getActivityList()
    }

    private fun getActivityList() {
        viewModelScope.launch {
            when (val data = testUseCase()) {
                is ActionResult.Success -> {
                    _list.emit(data.result)
                }
                is ActionResult.Error -> {
                    //_errorNullData.value = result.errors.errorMessage
                }
            }
        }
    }


}