package com.fiction.me.ui.fragments.explore.filterbytag

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetTagBooksByIdUseCase
import com.fiction.domain.model.BookInfoAdapterModelList
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksByTagViewModel(
    private val getTagBooksByIdUseCase: GetTagBooksByIdUseCase
) : BaseViewModel() {

    private val _bookList = MutableStateFlow<BookInfoAdapterModelList?>(null)
    val bookList = _bookList.asStateFlow()

    fun getTagBooksList(tagId: Long) {
        viewModelScope.launch {
            when (val result = getTagBooksByIdUseCase(tagId)) {
                is ActionResult.Success -> {
                    _bookList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateTagBooksListAdapter(id: Long, isSaved: Boolean) {
        val findItem = bookList.value?.books?.find { it.id == id }
        val index = if (findItem != null) bookList.value?.books?.indexOf(findItem) else -1

        if (index != -1 && index != null) {
            val updatedList = bookList.value?.books?.toMutableList().apply {
                this?.set(index, this[index].copy(isSaved = isSaved))
            }
            val newBookInfoItem = updatedList?.let { bookList.value?.copy(books = it) }
            viewModelScope.launch {
                _bookList.emit(newBookInfoItem)
            }
        }
    }
}