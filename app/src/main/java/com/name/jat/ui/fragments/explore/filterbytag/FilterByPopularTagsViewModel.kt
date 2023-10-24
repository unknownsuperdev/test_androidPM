package com.name.jat.ui.fragments.explore.filterbytag

import androidx.lifecycle.viewModelScope
import com.name.domain.model.PopularTagsDataModel
import com.name.domain.model.PopularTagsModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilterByPopularTagsViewModel : BaseViewModel() {

    private val _popularTagsList: MutableStateFlow<List<PopularTagsModel>?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val popularTagsList = _popularTagsList.asStateFlow()
    private val selectedTagsIdList = mutableListOf<PopularTagsDataModel>()

    fun getPopularTagsList() {

        val tagsList = mutableListOf<PopularTagsModel>()

        val popularTagsModel = PopularTagsModel(
            "9991", "Identity", listOf(
                PopularTagsDataModel(171, "Boss"),
                PopularTagsDataModel(172, "CEO"),
                PopularTagsDataModel(173, "Witch"),
                PopularTagsDataModel(174, "Vampire"),
                PopularTagsDataModel(175, "Warrior"),
                PopularTagsDataModel(176, "wizard"),
                PopularTagsDataModel(179, "gseg")
            )
        )
        val popularTagsModel1 = PopularTagsModel(
            "9992", "Character", listOf(
                PopularTagsDataModel(271, "Optimist"),
                PopularTagsDataModel(272, "Sweet"),
                PopularTagsDataModel(12411648, "Badboy"),
                PopularTagsDataModel(273, "Independent"),
                PopularTagsDataModel(20, "Optimist"),
                PopularTagsDataModel(274, "Sport")
            )
        )
        val popularTagsModel2 = PopularTagsModel(
            "9993", "Humor", listOf(
                PopularTagsDataModel(371, "Romance"),
                PopularTagsDataModel(372, "Myth"),
                PopularTagsDataModel(12417777, "Tragedy"),
                PopularTagsDataModel(373, "Suspense")
            )
        )
        val popularTagsModel3 = PopularTagsModel(
            "9994", "Setting", listOf(
                PopularTagsDataModel(471, "Marriage"),
                PopularTagsDataModel(472, "Magic"),
                PopularTagsDataModel(477777, "Crime"),
                PopularTagsDataModel(473, "Paranormal"),
                PopularTagsDataModel(22, "Betrayal")
            )
        )

        tagsList.add(popularTagsModel)
        tagsList.add(popularTagsModel1)
        tagsList.add(popularTagsModel2)
        tagsList.add(popularTagsModel3)

        viewModelScope.launch {
            _popularTagsList.emit(tagsList)
        }
    }

    fun updatePopularTagState(
        tagListItemId: String,
        tagId: Long,
        isSelected: Boolean
    ) {
        val updatedList = popularTagsList.value?.toMutableList()
        var findTagModel = updatedList?.find { it.id == tagListItemId }
        val tagModelIndex = updatedList?.indexOf(findTagModel) ?: -1
        var index = -1
        var isItemAddedInSelectedList = false

        if (findTagModel != null) {
            val findTagItem = findTagModel.popularTagsList.find { it.id == tagId }
            index =
                if (findTagItem != null) findTagModel.popularTagsList.indexOf(findTagItem) else -1
            if (index != -1) {

                if (!isSelected)
                    selectedTagsIdList.remove(findTagModel.popularTagsList[index])
                isItemAddedInSelectedList =
                    selectedTagsIdList.contains(findTagModel.popularTagsList[index])

                val findTagList = findTagModel.popularTagsList.toMutableList().apply {
                    this[index] = this[index].copy(isSelected = isSelected)
                }
                findTagModel = findTagModel.copy(popularTagsList = findTagList)
                if (tagModelIndex != -1)
                    updatedList?.set(tagModelIndex, findTagModel)
            }
        }

        if (isSelected && index != -1 && !isItemAddedInSelectedList)
            findTagModel?.popularTagsList?.get(index)?.let { selectedTagsIdList.add(it) }

        viewModelScope.launch {
            _popularTagsList.emit(updatedList)
        }
    }

    fun getSelectedTagsList() = selectedTagsIdList
    fun clearSelectedTagsList() = selectedTagsIdList.clear()
}


