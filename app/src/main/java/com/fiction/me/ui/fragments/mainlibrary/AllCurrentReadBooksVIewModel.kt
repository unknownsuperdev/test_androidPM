package com.fiction.me.ui.fragments.mainlibrary

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.model.AllCurrentReadBooksDataModel
import com.fiction.domain.model.OpenedAllBooks
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*

class AllCurrentReadBooksViewModel(
    private val getAllBooksLibraryUseCase: FlowPagingUseCase<OpenedAllBooks, AllCurrentReadBooksDataModel>
) : BaseViewModel() {

    fun getAllCurrentReadsBooksList(openedAllBooks: OpenedAllBooks) =
        MutableStateFlow(1)
            .map { getAllBooksLibraryUseCase(openedAllBooks) }
            .flatMapLatest { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
            .cachedIn(viewModelScope)

}
