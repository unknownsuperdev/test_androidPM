package com.fiction.me.ui.fragments.mainlibrary.finished

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.model.AllCurrentReadBooksDataModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*

class SeeAllFinishedBooksViewModel(
    private val getAllBooksLibraryUseCase: FlowPagingUseCase<Unit, AllCurrentReadBooksDataModel>
) : BaseViewModel() {

    val pagingData =
        MutableStateFlow(1)
            .map { getAllBooksLibraryUseCase(Unit) }
            .flatMapLatest { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
            .cachedIn(viewModelScope)
}