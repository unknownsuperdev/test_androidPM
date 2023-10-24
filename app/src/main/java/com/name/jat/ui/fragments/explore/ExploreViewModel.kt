package com.name.jat.ui.fragments.explore

import androidx.lifecycle.viewModelScope
import com.name.core.ActionResult
import com.name.domain.interactors.AddBookToLibraryUseCase
import com.name.domain.interactors.ExploreDataUseCase
import com.name.domain.interactors.GuestUseCase
import com.name.domain.interactors.RemoveBookFromLibraryUseCase
import com.name.domain.model.BaseExploreDataModel
import com.name.domain.model.StoryModel
import com.name.domain.model.SuggestedBooksModel
import com.name.domain.model.registration.GuestToken
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val guestUseCase: GuestUseCase,
    private val exploreDataUseCase: ExploreDataUseCase,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase
) : BaseViewModel() {

    init {
        getExploreDataList()
    }

    private val _exploreDataList: MutableStateFlow<List<BaseExploreDataModel>?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val exploreDataList = _exploreDataList.asStateFlow()

    private val _guestToken: MutableStateFlow<GuestToken?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val guestToken = _guestToken.asStateFlow()

    private val _printMessageAddedLib: MutableSharedFlow<Unit?> by lazy {
        MutableSharedFlow()
    }
    val printMessageAddedLib = _printMessageAddedLib.asSharedFlow()

    private val _printMessageRemoveLib: MutableSharedFlow<Unit?> by lazy {
        MutableSharedFlow()
    }
    val printMessageRemoveLib = _printMessageRemoveLib.asSharedFlow()

    fun getGuestToken(uuId: String) {
        viewModelScope.launch {
            when (val result = guestUseCase(uuId)) {
                is ActionResult.Success -> {
                    _guestToken.emit(result.result)
                }
                is ActionResult.Error -> {
                  //  TODO() // Error handling
                }
            }
        }
    }

    private fun getExploreDataList() {
        viewModelScope.launch {

            when (val result = exploreDataUseCase()) {
                is ActionResult.Success -> {
                    _exploreDataList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() // Error handling
                }
            }
        }
    }

    fun updateSuggestedBookItemData(
        dataModelId: String,
        bookItemId: Long,
        isAddedLibrary: Boolean,
        pos: Int,
        offset: Int
    ) {
        val updatedList = exploreDataList.value?.toMutableList()

        if (!updatedList.isNullOrEmpty()) {
            viewModelScope.launch {
                if (!isAddedLibrary) {
                    when (removeBookFromLibraryUseCase(bookItemId)) {
                        is ActionResult.Success -> {
                            updateExploreDataList(
                                dataModelId,
                                bookItemId,
                                isAddedLibrary,
                                pos,
                                offset,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            TODO() // Error handling
                        }
                    }
                } else {
                    when (addBookToLibraryUseCase(bookItemId)) {
                        is ActionResult.Success -> {
                            updateExploreDataList(
                                dataModelId,
                                bookItemId,
                                isAddedLibrary,
                                pos,
                                offset,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            TODO() // Error handling
                        }
                    }
                }
            }
        }
    }

    fun updateStoryItemData(
        dataModelId: String,
        storyItemId: Long,
        isAddedLibrary: Boolean,
        pos: Int,
        offset: Int
    ) {
        val updatedList = exploreDataList.value?.toMutableList()
        if (!updatedList.isNullOrEmpty()) {
            viewModelScope.launch {
                if (!isAddedLibrary) {
                    when (removeBookFromLibraryUseCase(storyItemId)) {
                        is ActionResult.Success -> {
                            updateStoryInExploreDataList(
                                dataModelId,
                                storyItemId,
                                isAddedLibrary,
                                pos,
                                offset,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            TODO() // Error handling
                        }
                    }
                } else {
                    when (addBookToLibraryUseCase(storyItemId)) {
                        is ActionResult.Success -> {
                            updateStoryInExploreDataList(
                                dataModelId,
                                storyItemId,
                                isAddedLibrary,
                                pos,
                                offset,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            TODO() // Error handling
                        }
                    }
                }
            }
        }
    }

    private fun updateExploreDataList(
        dataModelId: String,
        bookItemId: Long,
        isAddedLibrary: Boolean,
        pos: Int,
        offset: Int,
        updatedList: MutableList<BaseExploreDataModel>
    ) {
        for (i in updatedList.indices) {
            if (updatedList[i].id == dataModelId) {
                val itemSuggestedBooks = updatedList[i] as SuggestedBooksModel
                val findItem = itemSuggestedBooks.booksList.find { it.id == bookItemId }
                val index =
                    if (findItem != null) itemSuggestedBooks.booksList.indexOf(findItem) else -1

                if (findItem != null && index != -1) {
                    val bookList = itemSuggestedBooks.booksList.toMutableList().apply {
                        this[index] = this[index].copy(isAddedLibrary = isAddedLibrary)
                    }
                    updatedList[i] = itemSuggestedBooks.copy(
                        booksList = bookList,
                        position = pos,
                        offset = offset
                    )
                }
                break
            }
        }
        viewModelScope.launch {
            if (isAddedLibrary)
                _printMessageAddedLib.emit(Unit)
            else _printMessageRemoveLib.emit(Unit)
            _exploreDataList.emit(updatedList)
        }
    }

    private fun updateStoryInExploreDataList(
        dataModelId: String,
        storyId: Long,
        isAddedLibrary: Boolean,
        pos: Int,
        offset: Int,
        updatedList: MutableList<BaseExploreDataModel>
    ) {
        for (i in updatedList.indices) {
            if (updatedList[i].id == dataModelId) {
                val itemStory = updatedList[i] as StoryModel
                val findItem = itemStory.storyList.find { it.id == storyId }
                val index = if (findItem != null) itemStory.storyList.indexOf(findItem) else -1

                if (findItem != null && index != -1) {
                    val storyList = itemStory.storyList.toMutableList().apply {
                        this[index] = this[index].copy(isAddedLibrary = isAddedLibrary)
                    }

                    updatedList[i] = itemStory.copy(
                        storyList = storyList,
                        position = pos,
                        offset = offset
                    )
                }
                break
            }
        }
        viewModelScope.launch {
            if (isAddedLibrary)
                _printMessageAddedLib.emit(Unit)
            else _printMessageRemoveLib.emit(Unit)
            _exploreDataList.emit(updatedList)
        }
    }
}