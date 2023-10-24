package com.fiction.me.ui.fragments.explore.filterbytag

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fiction.core.ActionResult
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FilteredBooksViewModel(
    private val getFilteredTagsBooksUseCase: FlowPagingUseCase<List<Long>, BookInfoAdapterModel>,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase
) : BaseViewModel() {

    private val _selectedPopularTagsList = MutableStateFlow<List<PopularTagsDataModel>?>(null)
    val selectedPopularTagsList = _selectedPopularTagsList.asStateFlow()

    private val _printMessageAddedLib = MutableSharedFlow<Unit?>()
    val printMessageAddedLib = _printMessageAddedLib.asSharedFlow()

    private val _printMessageRemoveLib = MutableSharedFlow<Unit?>()
    val printMessageRemoveLib = _printMessageRemoveLib.asSharedFlow()

    private val _emptyState = MutableSharedFlow<Unit?>()
    val emptyState = _emptyState.asSharedFlow()

    val filteredBooksList = _selectedPopularTagsList
        .map {
            getFilteredTagsBooksUseCase(selectedPopularTagsList.value?.map { it.id } ?: emptyList())
        }
        .flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
        .cachedIn(viewModelScope)

    fun updateBookItemData(
        updatedList: List<BookInfoAdapterModel>,
        id: Long,
        isSaved: Boolean
    ) {
        if (updatedList.isNotEmpty()) {
            viewModelScope.launch {
                if (isSaved) {
                    when (addBookToLibraryUseCase(id)) {
                        is ActionResult.Success -> {
                            updateBookInfoListAdapter(
                                id,
                                isSaved,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                } else {
                    when (removeBookFromLibraryUseCase(id)) {
                        is ActionResult.Success -> {
                            updateBookInfoListAdapter(
                                id,
                                isSaved,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                }
            }
        }
    }

    private fun updateBookInfoListAdapter(
        id: Long,
        isSaved: Boolean,
        books: List<BookInfoAdapterModel>
    ) {
        books.map { if (it.id == id) it.isSaved = isSaved }
        viewModelScope.launch {
            if (isSaved)
                _printMessageAddedLib.emit(Unit)
            else _printMessageRemoveLib.emit(Unit)
        }
    }

    fun setSelectedTagsIdList(tags: List<PopularTagsDataModel>) {
        viewModelScope.launch {
            _selectedPopularTagsList.emit(tags.map { it.copy(isSelected = true) })
        }
    }

    fun updateSelectedItemsList(
        tagId: Long
    ) {
        val updatedList = selectedPopularTagsList.value?.filterNot { tagId == it.id }
        viewModelScope.launch {
            if (updatedList?.isEmpty() == true) _emptyState.emit(Unit)
            else _selectedPopularTagsList.emit(updatedList)
        }
    }

}