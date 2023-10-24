package com.fiction.me.ui.fragments.explore.filterbytag

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetAllTagsUseCase
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.domain.model.PopularTagsModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilterByPopularTagsViewModel(
    private val getAllTagsUseCase: GetAllTagsUseCase
) : BaseViewModel() {

    private val _popularTagsList = MutableStateFlow<List<PopularTagsModel>?>(null)
    val popularTagsList = _popularTagsList.asStateFlow()
    var isCleared = false

    var selectedTagsList = mutableListOf<PopularTagsDataModel>()

    fun getPopularTagsList() {
        viewModelScope.launch {
            when (val result = getAllTagsUseCase()) {
                is ActionResult.Success -> {
                    _popularTagsList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun clearSelectedTags(){
        viewModelScope.launch {
            isCleared = true
            _popularTagsList.emit(listOf())
        }
    }

    fun updatePopularTagState(
        tagListItemId: String,
        tagId: Long,
        isSelected: Boolean
    ) {
        val updatedList = popularTagsList.value?.toMutableList()?.map { tags ->
            if (tags.id == tagListItemId) {
                tags.copy(
                    popularTagsList = tags.popularTagsList.map {
                        if (it.id == tagId) {
                            val newItem =  it.copy(isSelected = isSelected)
                            updateSelectedList(newItem, isSelected)
                           newItem
                        } else it
                    }
                )
            }else tags
        }
        viewModelScope.launch {
            _popularTagsList.emit(updatedList)
        }
    }

    private fun updateSelectedList(tagItem: PopularTagsDataModel, isSelected: Boolean){
        if (isSelected) selectedTagsList.add(tagItem)
        else selectedTagsList = selectedTagsList.filter { it.id != tagItem.id }.toMutableList()
    }

    fun getIsThereSelectedTags() = selectedTagsList.isNotEmpty()
}


