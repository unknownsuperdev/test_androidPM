package com.name.jat.ui.fragments.mainlibrary.finished

import androidx.lifecycle.viewModelScope
import com.name.domain.model.BooksDataModel
import com.name.domain.model.BooksWithReadProgressBookData
import com.name.domain.model.SuggestedBooksModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinishedBooksViewModel : BaseViewModel() {
    private val _finishedReadBooks: MutableStateFlow<List<BooksWithReadProgressBookData>?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val finishedReadBooks = _finishedReadBooks.asStateFlow()

    private val _suggestedBooksList: MutableStateFlow<SuggestedBooksModel?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val suggestedBooksList = _suggestedBooksList.asStateFlow()

    fun getFinishedReadBooksList() {
        val bookList = listOf(
            BooksWithReadProgressBookData(
                90,
                "Day and night",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                100
            ),
            BooksWithReadProgressBookData(
                91,
                "Wife after love",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                100

            ),
            BooksWithReadProgressBookData(
                92,
                "One night with you",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                100
            ),
            BooksWithReadProgressBookData(
                93,
                "Day and night",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                100
            )
        )
        viewModelScope.launch {
            _finishedReadBooks.emit(bookList)
        }
    }

    fun getSuggestedBooksList() {
        val suggestedBooksModel = SuggestedBooksModel(
            "222", "You May Also Like", listOf(
                BooksDataModel(
                    4,
                    "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                    "Fantasy",
                    "Romance",
                    false
                ),
                BooksDataModel(
                    5,
                    "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                    "Fantasy",
                    "Romance",
                    false
                ),
                BooksDataModel(
                    6,
                    "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                    "Fantasy",
                    "Romance",
                    false
                ),
                BooksDataModel(
                    7,
                    "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                    "Fantasy",
                    "Romance",
                    false
                ),
                BooksDataModel(
                    8,
                    "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                    "Fantasy",
                    "Romance",
                    false
                )
            )
        )

        viewModelScope.launch {
            _suggestedBooksList.emit(suggestedBooksModel)
        }
    }

    fun updateSuggestedBooksList(dataModelId: Long, isAddedLibrary: Boolean) {
        var itemSuggestedBooks = suggestedBooksList.value
        val findItem = itemSuggestedBooks?.booksList?.find { it.id == dataModelId }
        val index = if (findItem != null) itemSuggestedBooks?.booksList?.indexOf(findItem) else -1

        if (index != -1 && index != null) {
            val updatedList = itemSuggestedBooks?.booksList?.toMutableList().apply {
                this?.set(index, this[index].copy(isAddedLibrary = isAddedLibrary))
            }
            itemSuggestedBooks = updatedList?.let { itemSuggestedBooks?.copy(booksList = it) }
        }

        viewModelScope.launch {
            _suggestedBooksList.emit(itemSuggestedBooks)
        }
    }
}